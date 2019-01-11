--- Maak de keystores en alle certificaten:
keytool -genkey -keystore serverkeystore.jks -storetype JKS -alias server -storepass passwd -validity 360 -keysize 2048 -keyalg RSA -dname "CN=localhost, OU=Unknown, O=Unknown, L=Unknown, S=Unknown, C=Unknown"
keytool -exportcert -alias server -keystore serverkeystore.jks -storepass passwd -keypass passwd -rfc -file server_cert.pem
keytool -import -keystore clienttruststore.jks -storetype JKS -file server_cert.pem -alias localhost -storepass passwd -noprompt
keytool -genkey -keystore clientkeystore.jks -storetype JKS -alias client -storepass passwd -validity 360 -keysize 2048 -keyalg RSA -dname "CN=client, OU=Unknown, O=Unknown, L=Unknown, S=Unknown, C=Unknown"
keytool -certreq -keystore clientkeystore.jks -alias client -keyalg rsa -file client.csr -storepass passwd
keytool -printcert -file client_cert.pem
keytool -gencert -keystore serverkeystore.jks -alias server  -storepass passwd -infile client.csr -outfile client_cert.pem -rfc
keytool -import -keystore servertruststore.jks -storetype JKS -file client_cert.pem -alias client_microservice -storepass passwd -noprompt

--- check formaat keystores
keytool -keystore serverkeystore.jks  -storepass passwd -list | grep type
keytool -keystore servertruststore.jks  -storepass passwd -list | grep type
keytool -keystore clientkeystore.jks  -storepass passwd -list | grep type
keytool -keystore clienttruststore.jks  -storepass passwd -list | grep type

--- verwijder losse certificaten en csr files
rm client.csr
rm client_cert.pem
rm server_cert.pem

--- verplaats files
mv serverkeystore.jks server\src\main\resources
mv servertruststore.jks server\src\main\resources
mv clientkeystore.jks client\src\main\resources
mv clienttruststore.jks client\src\main\resources

