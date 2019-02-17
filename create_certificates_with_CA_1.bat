rem --- Maak de keystores en alle certificaten:

rem maak CA, server, en clients keystores (met nieuwe keypairs)
keytool -genkey -keystore cakeystore.p12 -alias ca -storepass passwd -validity 360 -keysize 2048 -keyalg RSA -dname "CN=localhost, OU=Unknown, O=Unknown, L=Unknown, S=Unknown, C=Unknown"
keytool -genkey -keystore serverkeystore.p12 -alias server -storepass passwd -validity 360 -keysize 2048 -keyalg RSA -dname "CN=localhost, OU=Unknown, O=Unknown, L=Unknown, S=Unknown, C=Unknown"
keytool -genkey -keystore client1keystore.p12 -alias client -storepass passwd -validity 360 -keysize 2048 -keyalg RSA -dname "CN=client1, OU=Unknown, O=Unknown, L=Unknown, S=Unknown, C=Unknown"
keytool -genkey -keystore client2keystore.p12 -alias client -storepass passwd -validity 360 -keysize 2048 -keyalg RSA -dname "CN=client2, OU=Unknown, O=Unknown, L=Unknown, S=Unknown, C=Unknown"

rem Note (voor de server, gebruik de hostname waaronder de server draait als common name (CN))
rem Note (voor de clients, gebruik de naam waarmee je je wilt identificaten als common name (CN))

rem exporteer het CA certificaat en importeer deze in de truststore voor zowel de server als de clients
keytool -exportcert -alias ca -keystore cakeystore.p12 -storepass passwd -keypass passwd -rfc -file ca_cert.pem
keytool -import -keystore servertruststore.p12 -file ca_cert.pem -alias ca -storepass passwd -noprompt
keytool -import -keystore client1truststore.p12 -file ca_cert.pem -alias ca -storepass passwd -noprompt
keytool -import -keystore client2truststore.p12 -file ca_cert.pem -alias ca -storepass passwd -noprompt

rem genereer nu een CSR van alle drie de keystores
keytool -certreq -keystore serverkeystore.p12 -alias server -keyalg rsa -file server.csr -storepass passwd
keytool -certreq -keystore client1keystore.p12 -alias client -keyalg rsa -file client1.csr -storepass passwd
keytool -certreq -keystore client2keystore.p12 -alias client -keyalg rsa -file client2.csr -storepass passwd

rem sign de CSR's nu door de CA
keytool -gencert -keystore cakeystore.p12 -alias ca -storepass passwd -infile client1.csr -outfile client1_cert.pem -rfc
keytool -gencert -keystore cakeystore.p12 -alias ca -storepass passwd -infile client2.csr -outfile client2_cert.pem -rfc
keytool -gencert -keystore cakeystore.p12 -alias ca -storepass passwd -infile server.csr -outfile server_cert.pem -rfc

rem vervang nu het oude certificaat in de keystores, door de signed versie
keytool -import -keystore client1truststore.p12 -file client1_cert.pem -alias client -storepass passwd -noprompt
keytool -import -keystore client2truststore.p12 -file client2_cert.pem -alias server -storepass passwd -noprompt
keytool -import -keystore servertruststore.p12 -file server_cert.pem -alias server -storepass passwd -noprompt

rem --- check formaat keystores
keytool -keystore serverkeystore.p12  -storepass passwd -list | grep type
keytool -keystore servertruststore.p12  -storepass passwd -list | grep type
keytool -keystore client1keystore.p12  -storepass passwd -list | grep type
keytool -keystore client1truststore.p12  -storepass passwd -list | grep type
keytool -keystore client2keystore.p12  -storepass passwd -list | grep type
keytool -keystore client2truststore.p12  -storepass passwd -list | grep type

rem --- verwijder losse certificaten en csr files
rm *.pem
rm *.csr

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
