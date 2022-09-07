<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home</title>
</head>
<body>
<h1>Home</h1>
<h2>${SessionId}</h2>
<h2>Welcome to this page!</h2>
<h1>Profile</h1>
<h2>name: ${principal.getName()}</h2>
<h2> age: ${principal.getAge()}</h2>
<a href="${siteUrl}/profile/profile">Change Profile</a>
<br/>
<a href="${siteUrl}/auth/logout">Logout</a>
<body>
</html>