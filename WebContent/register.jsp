<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%!
ArrayList<String> provincie;
%>
<%
provincie = (ArrayList<String>) getServletContext().getAttribute("PROVINCIE");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Banca Del Tempo | Registrazione</title>
<!-- Tell the browser to be responsive to screen width -->
<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- Font Awesome -->
<link rel="stylesheet" href="plugins/fontawesome-free/css/all.min.css">
<!-- Ionicons -->
<link rel="stylesheet"
	href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
<!-- icheck bootstrap -->
<link rel="stylesheet"
	href="plugins/icheck-bootstrap/icheck-bootstrap.min.css">
<!-- Theme style -->
<link rel="stylesheet" href="dist/css/adminlte.min.css">
<!-- Google Font: Source Sans Pro -->
<link
	href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700"
	rel="stylesheet">
<link rel="stylesheet" href="plugins/sweetalert2/sweetalert2.min.css">
</head>
<body class="hold-transition register-page">
	<div class="register-box" style="width: 500px !important;">
		<div class="register-logo">
			<a href="index2.html"><b>Banca</b> Del Tempo</a>
		</div>

		<div class="card">
			<div class="card-body register-card-body">
				<p class="login-box-msg">Registra un nuovo socio</p>

				<form action="gestioneUtenti" method="post" autocomplete="off">
					<input type="hidden" name="cmd" value="registrazione">
					<div class="input-group mb-3">
						<input type="text" class="form-control" required="true"
							maxlength="40" name="txtNominativo" placeholder="Nome e cognome">
						<div class="input-group-append">
							<div class="input-group-text">
								<span class="fas fa-user"></span>
							</div>
						</div>
					</div>
					<div class="input-group mb-3">
						<input type="number" class="form-control" required="true"
							maxlength="15" name="txtTelefono" placeholder="Telefono"
							style="-webkit-appearance: none; margin: 0; -moz-appearance: textfield;">
						<div class="input-group-append">
							<div class="input-group-text">
								<span class="fas fa-phone"></span>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-6">
							<div class="input-group mb-3">
								<input type="text" class="form-control" required="true"
									name="txtIndirizzo" placeholder="Indirizzo">
								<div class="input-group-append">
									<div class="input-group-text">
										<span class="fas fa-search-location"></span>
									</div>
								</div>
							</div>
						</div>
						<div class="col-6">
							<div class="input-group mb-3">
								<input type="number" class="form-control" required="true"
									name="txtNumeroCivico" placeholder="Numero civico"
									style="-webkit-appearance: none; margin: 0; -moz-appearance: textfield;">
								<div class="input-group-append">
									<div class="input-group-text">
										&nbsp;
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-6">
							<div class="input-group mb-3">
								<select class="form-control" name="txtProvincia">
									<option selected="true" disabled="disabled">Provincia</option>    
								<%
								for(int i = 0; i < provincie.size(); i ++)
								{
								%>
									<option><%=provincie.get(i) %></option>
								<%
								}
								%>
								</select>
								<div class="input-group-append">
									<div class="input-group-text">
										&nbsp;
									</div>
								</div>
							</div>
						</div>
						<div class="col-6">
							<div class="input-group mb-3">
								<select class="form-control" name="txtComune" required="true">
									<option selected="true" disabled="disabled">Comune</option>
								</select>
								<div class="input-group-append">
									<div class="input-group-text">
										<span class="fas fa-search-location"></span>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="input-group mb-3">
						<input type="email" class="form-control" required="true"
							maxlength="40" name="txtEmail" placeholder="Email">
						<div class="input-group-append">
							<div class="input-group-text">
								&nbsp;
							</div>
						</div>
					</div>
					<div class="input-group mb-3">
						<input type="password" class="form-control" required="true"
							maxlength="40" name="txtPassword" placeholder="Password">
						<div class="input-group-append">
							<div class="input-group-text">
								<span class="fas fa-lock"></span>
							</div>
						</div>
					</div>
					<div class="input-group mb-3">
						<input type="password" class="form-control" required="true"
							name="txtRipetiPassword" placeholder="Ripeti password">
						<div class="input-group-append">
							<div class="input-group-text">
								<span class="fas fa-lock"></span>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-8">
							<div class="icheck-primary">
								<input type="checkbox" id="agreeTerms" name="terms"
									value="agree"> <label for="agreeTerms"> Accetto
									i termini di licenza </label>
							</div>
						</div>
						<!-- /.col -->
						<div class="col-4">
							<button type="submit" class="btn btn-primary btn-block">Registrati</button>
						</div>
						<!-- /.col -->
					</div>
				</form>

				<label for="remember"><a href="login.jsp"
					class="text-center">Sono già un socio</a></label>
			</div>
			<!-- /.form-box -->
		</div>
		<!-- /.card -->
	</div>
	<!-- /.register-box -->

	<!-- jQuery -->
	<script src="plugins/jquery/jquery.min.js"></script>
	<!-- Bootstrap 4 -->
	<script src="plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
	<!-- AdminLTE App -->
	<script src="dist/js/adminlte.min.js"></script>
	<script src="plugins/sweetalert2/sweetalert2.all.min.js"></script>


	<script>
		
		$("select[name='txtProvincia']").on("change", function() 
		{
			$.ajax(
			{
				url : "gestioneUtenti",
				type : "POST",
				data : 
				{
					cmd: "sceltaComune",
					provincia: this.value,
				},
				success : function(result) 
				{
					$("select[name='txtComune']").html(result);
				}
			});
		});

		$("form").submit(
						function(e) 
						{

							if ($(this).find('input[name="txtPassword"]').val() != $(this).find('input[name="txtRipetiPassword"]').val()) 
							{
								const Toast = Swal.mixin(
								{
									toast : true,
									position : "top-end",
									showConfirmButton : false,
									timer : 6000
								});
								Toast.fire(
								{
									icon : "error",
									title : "Il campo password e ripeti password non corrispondono"
								});

								return false;
							}

							if ($(this).find('input[name="terms"]')[0].checked === false) 
							{
								const Toast = Swal.mixin(
								{
									toast : true,
									position : "top-end",
									showConfirmButton : false,
									timer : 6000
								});
								Toast.fire(
								{
									icon : "error",
									title : "Devi accettare i termini di licenza"
								});

								return false;
							}
							
							
							return true;
						});
		
		
		<%
		if(request.getParameter("errore") != null)
		{
			%>
				const Toast = Swal.mixin({
					toast : true,
					position : "top-end",
					showConfirmButton : false,
					timer : 6000
				});
				Toast.fire({
					icon : "error",
					title : "Account con telefono o email inseriti già esistente"
				});
			<%
		}
		%>
	</script>
</body>
</html>
