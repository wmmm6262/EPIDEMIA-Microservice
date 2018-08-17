<html>
<head>
	<title>Welcome to EPIDEMIA Microservice System</title>
	<link href="webjars/bootstrap/3.3.6/css/bootstrap.min.css"
		rel="stylesheet">
	<link href="css/custom.css"
		rel="stylesheet">
</head>
<body>
	<div class="container">
		<table class="table table-striped">
			<caption>Your requests are</caption>
			<thead>
				<tr>
					<th>Description</th>
                    <th>Action</th>
				</tr>
			</thead>
			<tbody>
					<tr>
						<td>Upload Epidemiological Data</td>
						<td><a class="btn btn-warning" href="/epidata_update">Run</a></td>
					</tr>
                    <tr>
						<td>Update Environmental Data</td>
						<td><a class="btn btn-warning" href="/envdata_update">Run</a></td>
					</tr>
                    <tr>
						<td>Update Data</td>
						<td><a class="btn btn-warning" href="/data_update">Run</a></td>
					</tr>
                    <tr>
						<td>Integrate Environmental Data</td>
						<td><a class="btn btn-warning" href="/envdata_transfer">Run</a></td>
					</tr>
                    <tr>
						<td>Generate report</td>
						<td><a class="btn btn-warning" href="/report_generate">Run</a></td>
					</tr>
			</tbody>
		</table>
		<script src="webjars/jquery/1.9.1/jquery.min.js"></script>
		<script src="webjars/bootstrap/3.3.6/js/bootstrap.min.js"></script>
		<script src="js/custom.js"></script>
	</div>
</body>
</html>
