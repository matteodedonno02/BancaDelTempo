<%@page import="it.meucci.Categoria"%>
<%@page import="it.meucci.Prestazione"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Properties"%>
<%@page import="it.meucci.ManagerDB"%>
<%@page import="it.meucci.Utente"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%!
	Utente utente;
	ManagerDB db;
	Properties prop;
	Utente utenteDaModificare;
	Categoria categoriaDaModificare;
	String visualizza;
	ArrayList<Categoria> categorie;
%>
<%
	utente = (Utente)session.getAttribute("LOGGED_USER");
	if(utente == null || utente.getTipoUtente() == 0)
	{
		response.sendRedirect("../index.jsp");
		return;
	}
	
	
	visualizza = request.getParameter("elemento");
	
	
	prop = (Properties)getServletContext().getAttribute("PROPERTIES");
	db = new ManagerDB(prop.getProperty("db.host"), prop.getProperty("db.port"), prop.getProperty("db.database"), prop.getProperty("db.user"), prop.getProperty("db.password"));
	
	
	if(visualizza.equals("utenti"))
	{
		utenteDaModificare = db.utente(Integer.parseInt(request.getParameter("idUtente")));
		categorie = db.categorie();
		
		
		for(int i = 0; i < utenteDaModificare.getCategorie().size(); i ++)
		{
			for(int j = 0; j < categorie.size(); j ++)
			{
				if(utenteDaModificare.getCategorie().get(i).getIdCategoria() == categorie.get(j).getIdCategoria())
				{
					categorie.remove(j);
				}
			}
		}
	}
	else if(visualizza.equals("categorie"))
	{
		categoriaDaModificare = db.categoria(Integer.parseInt(request.getParameter("idCategoria")));
	}
	
	
	db.chiudiConnessione();
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>Banca Del Tempo | Modifica</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <!-- Font Awesome -->
  <link rel="stylesheet" href="../plugins/fontawesome-free/css/all.min.css">
  <!-- Ionicons -->
  <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href="../dist/css/adminlte.min.css">
  <!-- Google Font: Source Sans Pro -->
  <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700" rel="stylesheet">
  <link rel="shortcut icon" href="../dist/img/AdminLTELogo.png" type="image/x-icon">
  <link rel="stylesheet" href="../dist/css/my.css">
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
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
	          <span class="badge badge-warning navbar-badge">1</span>
	        </a>
	        <div class="dropdown-menu dropdown-menu-lg dropdown-menu-right">
	          <a href="#" class="dropdown-item">
	            <!-- Message Start -->
	            <div class="media">
	              <img src="../dist/img/user1-128x128.jpg" alt="User Avatar" class="img-size-50 mr-3 img-circle">
	              <div class="media-body">
	                <h3 class="dropdown-item-title">
	                  Brad Diesel
	                </h3>
	                <p class="text-sm">Call me whenever you can...</p>
	              </div>
	            </div>
	            <!-- Message End -->
	          </a>
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
      <img src="../dist/img/AdminLTELogo.png" alt="AdminLTE Logo" class="brand-image img-circle elevation-3" style="opacity: .8">
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
        	<a href="../profilo.jsp" class="d-block"><%=utente.getNominativo() %></a>
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
	            <a href="../sociindebito.jsp" class="nav-link">
	              <i class="nav-icon fas fa-history"></i>
	              <p>
	                Soci in debito
	              </p>
	            </a>
	          </li>
	          <li class="nav-item">
	            <a href="../richiediprestazione.jsp" class="nav-link">
	              <i class="nav-icon fas fa-th-list"></i>
	              <p>
	                Richiedi prestazione
	              </p>
	            </a>
	          </li>
	          <li class="nav-item">
	            <a href="../socisegreteria.jsp" class="nav-link">
	              <i class="nav-icon fas fa-print"></i>
	              <p>
	                Soci segreteria
	              </p>
	            </a>
	          </li>
	          <li class="nav-item">
		          <a href="../gestioneUtenti?cmd=logout" class="nav-link">
		              <i class="nav-icon fas fa-sign-out-alt"></i>
		              <p>
		                Esci
		              </p>
		          </a>
	          </li>
        <%
	        if(utente.getTipoUtente() == 1)
	    	{
	    	%>
	    		<li class="nav-header">SEZIONE AMMINISTRATORE</li>
	    		<li class="nav-item">
		            <a href="visualizza.jsp?elemento=utenti" class="nav-link">
		              <i class="nav-icon fas fa-users"></i>
		              <p>
		                Lista utenti
		              </p>
		            </a>
		          </li>
		          <li class="nav-item">
		            <a href="visualizza.jsp?elemento=categorie" class="nav-link">
		              <i class="nav-icon fas fa-layer-group"></i>
		              <p>
		                Lista categorie
		              </p>
		            </a>
		          </li>
		          <li class="nav-item">
		            <a href="visualizza.jsp?elemento=prestazioni" class="nav-link">
		              <i class="nav-icon fas fa-people-carry"></i>
		              <p>
		                Lista prestazioni
		              </p>
		            </a>
		          </li>
	    	<%
	    	}
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
            <%
            if(visualizza.equals("utenti"))
            {
            %>
            	<h1>Modifica profilo</h1>
            <%
            }
            else if(visualizza.equals("categorie"))
            {
            %>
            	<h1>Modifica categoria</h1>
            <%
            }
            %>
          </div>
        </div>
      </div><!-- /.container-fluid -->
    </section>

    <!-- Main content -->
    <section class="content">
      <div class="container-fluid">
        <%
        if(visualizza.equals("utenti"))
        {
        %>
        	<div class="row">
	          <div class="col-md-3">
	
	            <!-- Profile Image -->
	            <div class="card card-primary card-outline">
	              <div class="card-body box-profile">
	                <div class="text-center">
	                  <img class="profile-user-img img-fluid img-circle"
	                       src="https://image.flaticon.com/icons/svg/2919/2919600.svg"
	                       alt="User profile picture">
	                </div>
	                
	                <form autocomplete="off" action="../gestioneUtenti" method="post">
	                <input type="hidden" name="cmd" value="modificaUtente">
	                <input type="hidden" name="idUtente" value="<%=utenteDaModificare.getIdUtente() %>">
 	
	                <h3 class="profile-username text-center"><input class="clearinput" required="true" style="text-align: center;" type="text" name="txtNominativo" value="<%=utenteDaModificare.getNominativo() %>"></h3>
	
	                <ul class="list-group list-group-unbordered mb-3">
	                  <li class="list-group-item">
	                    <b>Email</b> <a class="float-right"><input type="email" class="clearinput" required="true" style="text-align: right;" name="txtEmail" value="<%=utenteDaModificare.getEmail() %>"></a>
	                  </li>
	                  <li class="list-group-item">
	                    <b>Telefono</b> <a class="float-right"><input type="number" class="clearinput" required="true" style="text-align: right;" name="txtTelefono" value="<%=utenteDaModificare.getTelefono() %>"></a>
	                  </li>
	                  <input type="submit" class="btn btn-primary btn-block" value="Salva">
	                 </form>
	                </ul>
	              </div>
	              <!-- /.card-body -->
	            </div>
	            <!-- /.card -->
	          </div>
	          <!-- /.col -->
	          <div class="col-md-9">
	            <div class="card">
	              <div class="card-header p-2">
	                <ul class="nav nav-pills">
	                	<li class="nav-item"><a class="nav-link active" href="#categorie" data-toggle="tab">Categorie</a></li>
	                </ul>
	              </div><!-- /.card-header -->
	              <div class="card-body">
	                <div class="tab-content">
	                  <div class="active tab-pane" id="categorie">
	                  	<h5>Categorie disponibili</h5>
	                  	<%
	                  	for(int i = 0; i < categorie.size(); i ++)
	                  	{
	                  	%>
	                  		<div class="row" style="margin-bottom: 10px;">
		                  		<div class="col-2">
		                  			<button id="<%=categorie.get(i).getIdCategoria() %>" type="button" class="aggiungiCategoria btn btn-primary btn-block"><i class="fas fa-plus-circle"></i></button>
		                  		</div>
		                  		<div class="col-10">
		                  			<%=categorie.get(i).getDescrizione() %>
		                  		</div>
	                  		</div>
	                  	<%
	                  	}
	                  	%>
	                  	<h5>Categorie dell'utente</h5>
	                  	<%
	                  	for(int i = 0; i < utenteDaModificare.getCategorie().size(); i ++)
	                  	{
	                  	%>
	                  		<div class="row" style="margin-bottom: 10px;">
		                  		<div class="col-2">
		                  			<button id="<%=utenteDaModificare.getCategorie().get(i).getIdCategoria() %>" type="button" class="rimuoviCategoria btn btn-primary btn-block"><i class="fas fa-minus-circle"></i></button>
		                  		</div>
		                  		<div class="col-10">
		                  			<%=utenteDaModificare.getCategorie().get(i).getDescrizione() %>
		                  		</div>
	                  		</div>
	                  	<%
	                  	}
	                  	%>
	                  </div>
	                </div>
	                <!-- /.tab-content -->
	              </div><!-- /.card-body -->
	            </div>
	            <!-- /.nav-tabs-custom -->
	          </div>
	          <!-- /.col -->
	        </div>
        <%
        }
        else if(visualizza.equals("categorie"))
        {
        %>
        	<div class="row">
	          <div class="col-md-12">
	
	            <!-- Profile Image -->
	            <div class="card card-primary card-outline">
	              <div class="card-body box-profile">
	                <div class="text-center">
	                </div>
	                
	                <form autocomplete="off" action="../gestioneCategorie" method="post">
		                <input type="hidden" name="cmd" value="modificaCategoria">
		                <input type="hidden" name="idCategoria" value="<%=categoriaDaModificare.getIdCategoria() %>">
			
		                <h3 class="profile-username text-center"><input class="clearinput" required="true" style="text-align: center;" type="text" name="txtDescrizione" value="<%=categoriaDaModificare.getDescrizione() %>"></h3>
		
		                <div class="col-4">
		                  <input type="submit" class="btn btn-primary btn-block" value="Salva">
		                </div>
	                </form>
	              </div>
	              <!-- /.card-body -->
	            </div>
	            <!-- /.card -->
	          </div>
	          <!-- /.col -->
	        </div>
        <%
        }
        %>
        <!-- /.row -->
      </div><!-- /.container-fluid -->
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
<script src="../plugins/jquery/jquery.min.js"></script>
<!-- Bootstrap 4 -->
<script src="../plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<!-- AdminLTE App -->
<script src="../dist/js/adminlte.min.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="../dist/js/demo.js"></script>
<script>
	<%
	if(utenteDaModificare != null)
	{
	%>
		$(".aggiungiCategoria").click(function(event) 
			{
				var idCategoria = event.target.id;
				var idUtente = <%=utenteDaModificare.getIdUtente() %>;
			    $.ajax(
				{
					url : "../gestioneUtenti",
					type : "POST",
					data : 
					{
						cmd: "aggiungiCategoria",
						idUtente: idUtente,
						idCategoria: idCategoria,
					},
					success : function(result) 
					{
						$("#categorie").html(result);
						location.reload();
					}
				});
			});
			
			
			$(".rimuoviCategoria").click(function(event) 
			{
				var idCategoria = event.target.id;
				var idUtente = <%=utenteDaModificare.getIdUtente() %>;
			    $.ajax(
				{
					url : "../gestioneUtenti",
					type : "POST",
					data : 
					{
						cmd: "rimuoviCategoria",
						idUtente: idUtente,
						idCategoria: idCategoria,
					},
					success : function(result) 
					{
						$("#categorie").html(result);
						location.reload();
					}
				});
			});
	<%
	}
	%>
</script>
</body>
</html>
