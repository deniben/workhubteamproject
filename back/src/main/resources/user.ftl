<#import "/spring.ftl" as spring/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

<h3>
    Dekanat list
</h3>
<br>
<div>
    <table border="2" bgcolor="f0f8ff">
        <tr>

        </tr>
        <#list users as user>
        </#list>
    </table>
    <a href="/dekanat/create">create</a>
    <a href="/dekanat/requestList">find</a>
</div>
</body>
</html>