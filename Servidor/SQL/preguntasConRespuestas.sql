SELECT p.idpreg,r.idresp,p.pregunta_tipo,p.sentenciaeng,p.sentenciaesp,r.sentenciaeng,r.sentenciaesp,re.puntaje,pc.idpaqcategoria,pc.categoria FROM
Pregunta p JOIN Respuesta r ON p.idpreg = r.idpreg 
LEFT JOIN RespuestaEspecifica re ON  r.idresp = re.idresp 
LEFT JOIN RespuestaGeneral_paquetecategoria rg ON r.idresp = rg.idresp 
FULL OUTER JOIN PaqueteCategoria pc ON rg.idpaqcategoria = pc.idpaqcategoria ORDER BY p.idpreg,r.idresp 