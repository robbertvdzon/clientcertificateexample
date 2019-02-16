Run create_certificates om alle certificaten te genereren.

Run server1 (zonder https, maar met basic authentication)
en
server2 (https, met required client certificate)

Als die draaien, kun je de client draaien. Die roept alle endpoints aan van beide servers, met en zonder Basic Authentication en met en zonder een client certificate.