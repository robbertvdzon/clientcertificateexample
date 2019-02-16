rem --- Maak de keystores en alle certificaten:

rem maak en exporteer server certificaat als server_cert.pem
keytool -genkey -keystore serverkeystore.p12 -alias server -storepass passwd -validity 360 -keysize 2048 -keyalg RSA -dname "CN=localhost, OU=Unknown, O=Unknown, L=Unknown, S=Unknown, C=Unknown"
keytool -exportcert -alias server -keystore serverkeystore.p12 -storepass passwd -keypass passwd -rfc -file server_cert.pem

rem importeer server certificaat in nieuwe client truststore
keytool -import -keystore client1truststore.p12 -file server_cert.pem -alias localhost -storepass passwd -noprompt
keytool -import -keystore client2truststore.p12 -file server_cert.pem -alias localhost -storepass passwd -noprompt

rem genereer een client keystore
keytool -genkey -keystore client1keystore.p12 -alias client -storepass passwd -validity 360 -keysize 2048 -keyalg RSA -dname "CN=client1, OU=Unknown, O=Unknown, L=Unknown, S=Unknown, C=Unknown"
keytool -genkey -keystore client2keystore.p12 -alias client -storepass passwd -validity 360 -keysize 2048 -keyalg RSA -dname "CN=client2, OU=Unknown, O=Unknown, L=Unknown, S=Unknown, C=Unknown"

rem maak een certificate request uit de client keystore(de client1.csr en client2.csr)
keytool -certreq -keystore client1keystore.p12 -alias client -keyalg rsa -file client1.csr -storepass passwd
keytool -certreq -keystore client2keystore.p12 -alias client -keyalg rsa -file client2.csr -storepass passwd

rem genereer een certificaat op basis van de certificate request (client_cert1.pem en client_cert2.pem)
keytool -gencert -keystore serverkeystore.p12 -alias server  -storepass passwd -infile client1.csr -outfile client1_cert.pem -rfc
keytool -gencert -keystore serverkeystore.p12 -alias server  -storepass passwd -infile client2.csr -outfile client2_cert.pem -rfc

rem importeer de nieuwe request in een nieuwe server trust store
keytool -import -keystore servertruststore.p12 -file client1_cert.pem -alias client1_microservice -storepass passwd -noprompt
keytool -import -keystore servertruststore.p12 -file client2_cert.pem -alias client2_microservice -storepass passwd -noprompt

rem keytool -printcert -file client_cert.pem

rem --- check formaat keystores
keytool -keystore serverkeystore.p12  -storepass passwd -list | grep type
keytool -keystore servertruststore.p12  -storepass passwd -list | grep type
keytool -keystore client1keystore.p12  -storepass passwd -list | grep type
keytool -keystore client1truststore.p12  -storepass passwd -list | grep type
keytool -keystore client2keystore.p12  -storepass passwd -list | grep type
keytool -keystore client2truststore.p12  -storepass passwd -list | grep type

rem --- verwijder losse certificaten en csr files
rm client1.csr
rm client2.csr
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
