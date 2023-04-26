Trabalho realizado por:
fc54409 Miguel Reis
fc54446 Ricardo Soares
fc54933 Francisco Carrapa

--TINTOLMARKET--

Para compilar o projeto:
Abrir dois terminais, mudar o diretório para o diretório dos respetivos ficheiros .jar.
Para o servidor colocar no terminal:
$ java -jar TintolmarketServer.jar <port> <password-cifra> <keystore> <password-keystore>
Para o cliente:
$ java -jar Tintolmarket.jar <serverAddress> <truststore> <keystore> <password-keystore> <userID>

No servidor, o port deve ser 12345. O campo password-cifra serve para cifrar o ficheiro users.txt.
A keystore contém o par de chaves RSA 2048 bits a ser usado pelo servidor, sendo a password-keystore
a password para aceder à mesma -> utilizar keystore.server, com a password keypass

No cliente, o <serverAddress> tem o formato <IP:hostname>[:Port], onde o Port é opcional,
sendo por omissão 12345.
<truststore> que contém os certificados de chave pública do servidor e de outros clientes -> no cliente, usar
o ficheiro truststore.client
<keystore> que contém o par de chaves do userID; -> utilizar uma das keystores acabadas em userX -> ao usar a keyStore.userX,
o userID tambem terá que ser userX, para o acesso correto à chave privada na keystore.
<password-keystore> é a password da keystore;
<userID> é o username do utilizador.


Os parâmetros dentro de "<>" são obrigatórios.

O ficheiro users.txt, como pedido no enunciado, guarda os utilizadores e as suas respetivas
passwords, sendo agora cifrado, tal como pedido.

O ficheiro wines.ser guarda a informação serializada do Objeto catWine, responsável esse por 
guardar as informações respetivas de cada vinho.

O ficheiro wallet.ser guarda a informação serializada do Objeto catWallet, responsável esse por
guardar as informações respetivas de cada wallet(utilizador)

Por fim, o ficheiro messages.txt guarda a informação relativa às mensagens dos utilizadores.

Quando um vinho é adicionado (OP: ADD) o servidor guarda a imagem do vinho na pasta vinhos.
Quando um vinho é visualizado (OP: VIEW) o cliente guarda a imagem do vinho num ficheiro na pasta respetiva do utilizador.

Quando uma operação sell ou buy é realizada, uma Transação é adicionada à blockchain, sendo a blockchain guardada na
pasta respetiva.

--KEYSTORES, ALIAS E PASSWORDS--
keystore.server - myServer - keypass
keystore.user1 - user1 - user1pass
keystore.user2 - user2 - user2pass

Limitações:
Quando o Servidor fecha, o cliente fecha com erros.
Não verificamos se dá para fazer a conexão entre cliente servidor através de diferentes máquinas, contudo achamos
que está funcional.
