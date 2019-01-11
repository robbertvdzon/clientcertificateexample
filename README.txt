--- Maak de keystores en alle certificaten:
keytool -genkey -keystore serverkeystore.p12 -alias server -storepass passwd -validity 360 -keysize 2048 -keyalg RSA -dname "CN=localhost, OU=Unknown, O=Unknown, L=Unknown, S=Unknown, C=Unknown"
keytool -exportcert -alias server -keystore serverkeystore.p12 -storepass passwd -keypass passwd -rfc -file server_cert.pem
keytool -import -keystore clienttruststore.p12 -file server_cert.pem -alias localhost -storepass passwd -noprompt
keytool -genkey -keystore clientkeystore.p12 -alias client -storepass passwd -validity 360 -keysize 2048 -keyalg RSA -dname "CN=client, OU=Unknown, O=Unknown, L=Unknown, S=Unknown, C=Unknown"
keytool -certreq -keystore clientkeystore.p12 -alias client -keyalg rsa -file client.csr -storepass passwd
keytool -printcert -file client_cert.pem
keytool -gencert -keystore serverkeystore.p12 -alias server  -storepass passwd -infile client.csr -outfile client_cert.pem -rfc
keytool -import -keystore servertruststore.p12 -file client_cert.pem -alias client_microservice -storepass passwd -noprompt

--- check formaat keystores
keytool -keystore serverkeystore.p12  -storepass passwd -list | grep type
keytool -keystore servertruststore.p12  -storepass passwd -list | grep type
keytool -keystore clientkeystore.p12  -storepass passwd -list | grep type
keytool -keystore clienttruststore.p12  -storepass passwd -list | grep type

--- verwijder losse certificaten en csr files
rm client.csr
rm client_cert.pem
rm server_cert.pem

--- verplaats files
mv serverkeystore.p12 server\src\main\resources
mv servertruststore.p12 server\src\main\resources
mv clientkeystore.p12 client\src\main\resources
mv clienttruststore.p12 client\src\main\resources

