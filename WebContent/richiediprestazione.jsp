<%@page import="it.meucci.Prestazione"%>
<%@page import="it.meucci.Categoria"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.meucci.Zona"%>
<%@page import="java.util.Properties"%>
<%@page import="it.meucci.ManagerDB"%>
<%@page import="it.meucci.Utente"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%!
	Utente utente;
	Zona zona;
	Properties prop;
	ManagerDB db;
	ArrayList<Categoria> categorie;
	ArrayList<Prestazione> prestazioniDaApprovare;
	ArrayList<Prestazione> prestazioniDaConcludere;
%>
<%
	utente = (Utente)session.getAttribute("LOGGED_USER");
	if(utente == null)
	{
		response.sendRedirect("index.jsp");
		return;
	}
	
	
	prop = (Properties)getServletContext().getAttribute("PROPERTIES");
	db = new ManagerDB(prop.getProperty("db.host"), prop.getProperty("db.port"), prop.getProperty("db.database"), prop.getProperty("db.user"), prop.getProperty("db.password"));
	zona = db.zonaDaId(utente.getIdZona());
	categorie = db.categorie();
	prestazioniDaApprovare = db.prestazioniDaApprovare(utente.getIdUtente());
	prestazioniDaConcludere = db.prestazioniDaConcludere(utente.getIdUtente());
	db.chiudiConnessione();
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>Banca Del Tempo | Richiedi prestazione</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <!-- Font Awesome -->
  <link rel="stylesheet" href="plugins/fontawesome-free/css/all.min.css">
  <!-- Ionicons -->
  <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
  <!-- DataTables -->
  <link rel="stylesheet" href="plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href="dist/css/adminlte.min.css">
  <!-- Google Font: Source Sans Pro -->
  <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700" rel="stylesheet">
  <link href="dist/css/my.css" rel="stylesheet">
  <link rel="shortcut icon" href="dist/img/AdminLTELogo.png" type="image/x-icon">
</head>
<body class="hold-transition sidebar-mini">
	<div class="modal fade" id="modal-default">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h4 class="modal-title">Richiedi prestazione</h4>
              <button id="chiudiRichiesta1" type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
            	<form autocomplete="off" id="richiesta">
            		<div class="input-group mb-3">
						<input name="txtOre" required="true" min="1" type="number" class="form-control" id="orePrestazione" placeholder="Ore">
						<div class="input-group-append">
							<div class="input-group-text">
								<span class="far fa-clock"></span>
							</div>
						</div>
					</div>
					<div class="input-group mb-3">
						<input name="txtData" required="true" type="date" class="form-control" id="dataPrestazione">
						<div class="input-group-append">
							<div class="input-group-text">
								<span class="far fa-calendar-alt"></span>
							</div>
						</div>
					</div>
					<div class="form-group">
                        <textarea name="txtDescrizione" required="true" class="form-control" rows="3" id="descrizionePrestazione" placeholder="Descrizione"></textarea>
                    </div>
            </div>
            <div class="modal-footer justify-content-between">
              <button id="chiudiRichiesta" type="button" class="btn btn-default" data-dismiss="modal">Chiudi</button>
              <button type="submit" class="btn btn-primary">Richiedi</button>
            </div>
            	</form>
          </div>
          <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
      </div>


<div class="wrapper">

  <!-- Navbar -->
  <nav class="main-header navbar navbar-expand navbar-white navbar-light">
    <!-- Left navbar links -->
    <ul class="navbar-nav">
      <li class="nav-item">
        <a class="nav-link" data-widget="pushmenu" href="#" role="button"><i class="fas fa-bars"></i></a>
      </li>
    </ul>

    <!-- Right navbar links -->
	<!--ZONA NOTIFICHE -->
    <%
    if(utente != null)
    {
    %>
    	<ul class="navbar-nav ml-auto">
	      <!-- Notifications Dropdown Menu -->
	      <li class="nav-item dropdown">
	        <a class="nav-link" data-toggle="dropdown" href="#">
	          <i class="far fa-bell"></i>
	          <span class="badge badge-warning navbar-badge"><%=prestazioniDaApprovare.size() + prestazioniDaConcludere.size() %></span>
	        </a>
	        <div class="dropdown-menu dropdown-menu-lg dropdown-menu-right">
	            <%
	            for(int i = 0; i < prestazioniDaApprovare.size(); i ++)
	            {
	            %>
	            	<a href="profilo.jsp#richieste" class="dropdown-item">
	            	<!-- Message Start -->
		            <div class="media">
		              <div class="media-body">
		                <h3 class="dropdown-item-title">
		                  <b>RICHIESTA DA</b> <%=prestazioniDaApprovare.get(i).getFruitore().getNominativo() %>
		                </h3>
		                <p class="text-sm"><%=prestazioniDaApprovare.get(i).getDescrizione() %></p>
		              </div>
		            </div>
		            <!-- Message End -->
	          		</a>
	            <%
	            }
	            for(int i = 0; i < prestazioniDaConcludere.size(); i ++)
	            {
	            %>
	            	<a href="profilo.jsp#richieste" class="dropdown-item">
	            	<!-- Message Start -->
		            <div class="media">
		              <div class="media-body">
		                <h3 class="dropdown-item-title">
		                  <b>DA CONCLUDERE</b> <%=prestazioniDaConcludere.get(i).getFruitore().getNominativo() %>
		                </h3>
		                <p class="text-sm"><%=prestazioniDaConcludere.get(i).getDescrizione() %></p>
		              </div>
		            </div>
		            <!-- Message End -->
	          		</a>
	            <%
	            }
	            %>
	          <div class="dropdown-divider"></div>
	        </div>
	      </li>
	    </ul>
    <%
    }
    %>
  </nav>
  <!-- /.navbar -->

  <!-- Main Sidebar Container -->
  <aside class="main-sidebar sidebar-dark-primary elevation-4">
    <!-- Brand Logo -->
    <a href="index.jsp" class="brand-link">
      <img src="dist/img/AdminLTELogo.png" alt="AdminLTE Logo" class="brand-image img-circle elevation-3" style="opacity: .8">
      <span class="brand-text font-weight-light"><b>Banca</b> Del Tempo</span>
    </a>

    <!-- Sidebar -->
    <div class="sidebar">
      <!-- Sidebar user panel (optional) -->
      <div class="user-panel mt-3 pb-3 mb-3 d-flex">
        <div class="image">
          <img src="https://image.flaticon.com/icons/svg/2919/2919600.svg" class="img-circle elevation-2" alt="User Image">
        </div>
        <div class="info">
        <%
        if(utente == null)
        {
        %>
        	<a href="" class="d-block">Ospite</a>
        <%
        }
        else
        {
        %>
        	<a href="profilo.jsp" class="d-block"><%=utente.getNominativo() %></a>
        <%
        }
        %>
        </div>
      </div>

      <!-- Sidebar Menu -->
      <nav class="mt-2">
        <ul class="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu" data-accordion="false">
        <%
        if(utente == null)
        {
        %>
        	<li class="nav-item">
	          <a href="login.jsp" class="nav-link">
	              <i class="nav-icon fas fa-sign-in-alt"></i>
	              <p>
	                Login
	              </p>
	          </a>
          	</li>
          	
          	<li class="nav-item">
	          <a href="register.jsp" class="nav-link">
	              <i class="nav-icon fas fa-user-plus"></i>
	              <p>
	                Registrati
	              </p>
	          </a>
          	</li>
        <%
        }
        else
        {
        %>
        	<li class="nav-item">
	            <a href="sociindebito.jsp" class="nav-link">
	              <i class="nav-icon fas fa-history"></i>
	              <p>
	                Soci in debito
	              </p>
	            </a>
	          </li>
	          <li class="nav-item">
	            <a href="richiediprestazione.jsp" class="nav-link active">
	              <i class="nav-icon fas fa-th-list"></i>
	              <p>
	                Richiedi prestazione
	              </p>
	            </a>
	          </li>
	          <li class="nav-item">
	            <a href="socisegreteria.jsp" class="nav-link">
	              <i class="nav-icon fas fa-print"></i>
	              <p>
	                Soci segreteria
	              </p>
	            </a>
	          </li>
	          <li class="nav-item">
		          <a href="gestioneUtenti?cmd=logout" class="nav-link">
		              <i class="nav-icon fas fa-sign-out-alt"></i>
		              <p>
		                Esci
		              </p>
		          </a>
	          </li>
        <%
        }
	        if(utente.getTipoUtente() == 1)
	    	{
	    	%>
	    		<li class="nav-header">SEZIONE AMMINISTRATORE</li>
	    		<li class="nav-item">
		            <a href="admin/visualizza.jsp?elemento=utenti" class="nav-link">
		              <i class="nav-icon fas fa-users"></i>
		              <p>
		                Lista utenti
		              </p>
		            </a>
		          </li>
		          <li class="nav-item">
		            <a href="admin/visualizza.jsp?elemento=categorie" class="nav-link">
		              <i class="nav-icon fas fa-layer-group"></i>
		              <p>
		                Lista categorie
		              </p>
		            </a>
		          </li>
		          <li class="nav-item">
		            <a href="admin/visualizza.jsp?elemento=prestazioni" class="nav-link">
		              <i class="nav-icon fas fa-people-carry"></i>
		              <p>
		                Lista prestazioni
		              </p>
		            </a>
		          </li>
	    	<%
	    	}
        %>
          
        </ul>
      </nav>
      <!-- /.sidebar-menu -->
    </div>
    <!-- /.sidebar -->
  </aside>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>Richiedi prestazione</h1>
          </div>
        </div>
      </div><!-- /.container-fluid -->
    </section>

    <!-- Main content -->
    <section class="content" style="padding-bottom: 40px;">
      <div class="container-fluid">
      	<h4 style="padding-bottom: 20px;">La tua zona : <b><%=zona.getDescrizione() %></b></h4>
      	<%
      	if(zona.getHtmlMappa() == null)
      	{
      	%>
      		<h5>Mappa non disponibile</h5>
      	<%
      	}
      	else
      	{
      	%>
          	<%=zona.getHtmlMappa() %>
        <%
      	}
      	%>
      	<form id="formCategoria" action="gestionePrestazioni" method="post" autocomplete="off">
      		<input type="hidden" name="cmd" value="selezioneCategoria">
      		<div class="input-group mb-3">
					<div class="col-6">
						<select class="form-control" name="txtCategoria" required="true">
							<option selected="true" disabled="disabled">Categoria</option>
							<%
							for(int i = 0; i < categorie.size(); i ++)
							{
							%>
								<option><%=categorie.get(i).getDescrizione() %></option>
							<%
							}
							%>
						</select>
					</div>
					<div class="4">
						<input type="submit" class="btn btn-block btn-default btn-sm" value="Cerca">
					</div>
			</div>
      	</form>
      	
      	
      	<div id="tabellaRisultati">
      		
      	</div>
      </div>
    </section>
      	
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->
  <footer class="main-footer">
    <strong>Esami di stato 2020 Matteo De Donno  &copy; , 5BI</strong>
  </footer>

  <!-- Control Sidebar -->
  <aside class="control-sidebar control-sidebar-dark">
    <!-- Control sidebar content goes here -->
  </aside>
  <!-- /.control-sidebar -->
</div>
<!-- ./wrapper -->

<!-- jQuery -->
<script src="plugins/jquery/jquery.min.js"></script>
<!-- Bootstrap 4 -->
<script src="plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<!-- DataTables -->
<script src="plugins/datatables/jquery.dataTables.min.js"></script>
<script src="plugins/datatables-bs4/js/dataTables.bootstrap4.min.js"></script>
<script src="plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
<script src="plugins/datatables-responsive/js/responsive.bootstrap4.min.js"></script>
<!-- AdminLTE App -->
<script src="dist/js/adminlte.min.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="dist/js/demo.js"></script>
<script src="http://malsup.github.com/jquery.form.js"></script>
<!-- page script -->
<script>
	debugger;
	var d = new Date();
	var day = d.getDate();
	if(day.toString().length == 1)
	{
		day = 0 + day.toString();
	}
	var month = d.getMonth() + 1;
	if(month.toString().length == 1)
	{
		month = 0 + month.toString();
	}
	var year = d.getFullYear();
	var dateStr = year + "-" + month + "-" + day;
	
	
	$("#dataPrestazione").attr("min", dateStr);


	$("#formCategoria").ajaxForm(
	{
		url: "gestionePrestazioni",
		type: "post",
		success : function(result) 
		{
			$("#tabellaRisultati").html(result);
			$(function () 
			{
			    $("#example1").DataTable(	
			    {
			      "responsive": true,
			      "autoWidth": false,
			    });
			    $('#example2').DataTable({
			      "paging": true,
			      "lengthChange": false,
			      "searching": false,
			      "ordering": true,
			      "info": true,
			      "autoWidth": false,
			      "responsive": true,
			      "language": 
			      {
			            "zeroRecords": "Nessun dato trovato",
			            "paginate": 
			            {
			            	"previous": "Precedente",
			            	"next": "Successivo"
			            }
			        }
			    });
			    
			    
			    loadScript();
		  	});
		}
	});
	
	
	function loadScript()
	{
		$("#chiudiRichiesta, #chiudiRichiesta1").click(function()
		{
			$("#richiesta").find("input, textarea").val("");
		});


		
		var formId;
		$(".richiediPrestazione").click(function(event)
		{
			$("#modal-default").modal("show");
			formId = $(this).attr("id").replace("richiediPrestazione", "");
			
			
			return false;
		});
		

		$("#modal-default").on("hidden.bs.modal", function () 
		{
			if($("#richiesta").find("input, textarea").val() == "")
			{
				delete formId;
				$("#richiesta").find("input, textarea").val("");
				return;	
			}
			
			
			$("#richiesta").find(":input").clone().appendTo("#risultato" + formId);
			$("#richiesta").find("input").attr("type", "hidden");
			$("#richiesta").find("textarea").css("display", "none");
			$("#richiesta").find("input, textarea").val("");
			$("#risultato" + formId).submit();
		});


		$("#richiesta").submit(function(e)
		{
			$("#modal-default").modal("hide");
		    e.preventDefault();
		});
	}
</script>
</body>
</html>
