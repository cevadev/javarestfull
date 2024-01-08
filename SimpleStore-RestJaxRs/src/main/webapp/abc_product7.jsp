<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cat&aacute;logo de productos</title>
    </head>
    <body>
        
<div align="center">
    <!-- Recordemos que RESTFULL maneja los verbos GET, POST, PUT pero html solo
         soporta GET y POST  
         si product no tiene id es un dato nuevo
         si product posee id es una actualizacion-->
    <form method="post" action="<c:url value="/rest/product/${data.primaryKey}"/>">
    <input type="hidden" name="id_product" value="${data.primaryKey}">
    Nombre: <input type="text" name="name" value="${data.name}"> ${data.getError("name")} <br>
    Descripci&oacute;n: <input type="text" name="description" value="${data.description}"> ${data.getError("description")}<br>
    Precio: <input type="text" name="price" value="${data.price}"> ${data.getError("price")}<br>
    <input type="submit" value="Guardar"><p>
    
        <a href="<c:url value="/rest/product/" />">Cancelar</a>
</form>
</div>

<div align="center">
    <p>
    <a href="<c:url value="/product/" />">Regresar</a>
</div>
    
    </body>
</html>