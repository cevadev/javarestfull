<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cat&aacute;logo de productos</title>
    </head>
    <body>
        
<div align="center"><h3>Cat&aacute;logo de productos versi&oacute;n 8++</h3></div>
        
<table align="center" border="1">
    <tr>
        <th>id_product</th><th>name</th><th>description</th><th>price</th><th>&nbsp;</th>
    </tr>
<c:forEach items="${data}" var="pd">
<tr>
    <td>${pd.primaryKey}</td>
    <td>${pd.name}</td>
    <td>${pd.description}</td>
    <td align="right">${pd.price}</td>
    <td><a href="<c:url value="/product/${pd.primaryKey}"/>">[Ed]</a>
        <a href="<c:url value="/product/${pd.primaryKey}?action=del"/>">[X]</a></td>
</tr>
</c:forEach>
</table>
<p>
<div align="center">${pageBar}</div>

<div align="center">
<p>
    <!-- si colocamos product pero sin parametros retorn el form de captura de product -->
    <a href="<c:url value="/abc_product7.jsp" />">Nuevo</a><p>
<a href="<c:url value="/index.html" />">Regresar</a>
</div>

    </body>
</html>