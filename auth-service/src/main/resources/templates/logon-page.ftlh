<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Logon</title>
</head>
<body>
<h1>Logon</h1>
<h2>${SessionId}</h2>
<form action="${siteUrl}/auth/logon-form" method="POST" target="_self">
Name: <input type="text" name="name" value="">
<br/>
Password: <input type="password" name="passwd" value="">
<br/>
<input type="submit">
</form>
<body>
</html>