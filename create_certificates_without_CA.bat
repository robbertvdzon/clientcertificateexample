rem --- Maak de keystores en alle certificaten:

rem maak server, en clients keystores (met nieuwe keypairs)
keytool -genkey -keystore serverkeystore.p12 -alias server -storepass passwd -validity 360 -keysize 2048 -keyalg RSA -dname "CN=localhost, OU=Unknown, O=Unknown, L=Unknown, S=Unknown, C=Unknown"
keytool -genkey -keystore client1keystore.p12 -alias client -storepass passwd -validity 360 -keysize 2048 -keyalg RSA -dname "CN=client1, OU=Unknown, O=Unknown, L=Unknown, S=Unknown, C=Unknown"
keytool -genkey -keystore client2keystore.p12 -alias client -storepass passwd -validity 360 -keysize 2048 -keyalg RSA -dname "CN=client2, OU=Unknown, O=Unknown, L=Unknown, S=Unknown, C=Unknown"

rem Note (voor de server, gebruik de hostname waaronder de server draait als common name (CN))
rem Note (voor de clients, gebruik de naam waarmee je je wilt identificaten als common name (CN))

rem exporteer de certificaten
keytool -exportcert -alias server -keystore serverkeystore.p12 -storepass passwd -keypass passwd -rfc -file server_cert.pem
keytool -exportcert -alias client -keystore client1keystore.p12 -storepass passwd -keypass passwd -rfc -file client1_cert.pem
keytool -exportcert -alias client -keystore client2keystore.p12 -storepass passwd -keypass passwd -rfc -file client2_cert.pem

rem importeer de client certificaten in de (nieuwe) truststore van de server
keytool -import -keystore servertruststore.p12 -file client1_cert.pem -alias client1_microservice -storepass passwd -noprompt
keytool -import -keystore servertruststore.p12 -file client2_cert.pem -alias client2_microservice -storepass passwd -noprompt

rem importeer de server certificaat in de 2 client truststores
keytool -import -keystore client1truststore.p12 -file server_cert.pem -alias localhost -storepass passwd -noprompt
keytool -import -keystore client2truststore.p12 -file server_cert.pem -alias localhost -storepass passwd -noprompt

rem --- check formaat keystores
keytool -keystore serverkeystore.p12  -storepass passwd -list | grep type
keytool -keystore servertruststore.p12  -storepass passwd -list | grep type
keytool -keystore client1keystore.p12  -storepass passwd -list | grep type
keytool -keystore client1truststore.p12  -storepass passwd -list | grep type
keytool -keystore client2keystore.p12  -storepass passwd -list | grep type
keytool -keystore client2truststore.p12  -storepass passwd -list | grep type

rem --- verwijder losse certificaten en csr files
rm client1_cert.pem
rm client2_cert.pem
rm server_cert.pem

rem --- verplaats files
cp serverkeystore.p12 server2\src\main\resources
cp servertruststore.p12 server2\src\main\resources
cp client1keystore.p12 client\src\main\resources
cp client1truststore.p12 client\src\main\resources
cp client2keystore.p12 client\src\main\resources
cp client2truststore.p12 client\src\main\resources

rem -- remove files
rm serverkeystore.p12
rm servertruststore.p12
rm client1keystore.p12
rm client2keystore.p12
rm client1truststore.p12
rm client2truststore.p12
