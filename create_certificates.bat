rem --- Maak de keystores en alle certificaten:
keytool -genkey -keystore serverkeystore.p12 -alias server -storepass passwd -validity 360 -keysize 2048 -keyalg RSA -dname "CN=localhost, OU=Unknown, O=Unknown, L=Unknown, S=Unknown, C=Unknown"
keytool -exportcert -alias server -keystore serverkeystore.p12 -storepass passwd -keypass passwd -rfc -file server_cert.pem
keytool -import -keystore clienttruststore.p12 -file server_cert.pem -alias localhost -storepass passwd -noprompt
keytool -genkey -keystore clientkeystore.p12 -alias client -storepass passwd -validity 360 -keysize 2048 -keyalg RSA -dname "CN=client, OU=Unknown, O=Unknown, L=Unknown, S=Unknown, C=Unknown"
keytool -certreq -keystore clientkeystore.p12 -alias client -keyalg rsa -file client.csr -storepass passwd
keytool -gencert -keystore serverkeystore.p12 -alias server  -storepass passwd -infile client.csr -outfile client_cert.pem -rfc
keytool -import -keystore servertruststore.p12 -file client_cert.pem -alias client_microservice -storepass passwd -noprompt
rem keytool -printcert -file client_cert.pem

rem --- check formaat keystores
keytool -keystore serverkeystore.p12  -storepass passwd -list | grep type
keytool -keystore servertruststore.p12  -storepass passwd -list | grep type
keytool -keystore clientkeystore.p12  -storepass passwd -list | grep type
keytool -keystore clienttruststore.p12  -storepass passwd -list | grep type

rem --- verwijder losse certificaten en csr files
rm client.csr
rm client_cert.pem
rm server_cert.pem

rem --- verplaats files
cp serverkeystore.p12 server2\src\main\resources
cp servertruststore.p12 server2\src\main\resources
cp clientkeystore.p12 client\src\main\resources
cp clienttruststore.p12 client\src\main\resources
cp serverkeystore.p12 javalinserver1\src\main\resources
cp servertruststore.p12 javalinserver1\src\main\resources

rem -- remove files
rm serverkeystore.p12
rm servertruststore.p12
rm clientkeystore.p12
rm clienttruststore.p12
