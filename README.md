OBSERVAÇÕES

- Por mais que o teste tenha dito que banco de dados não era uma necessidade eu optei por utilzar o H2 como o mesmo para uma correta e mais precisa manipulação de dados. H2 é um BD em memória, então não é necessário a criação do mesmo
- O desafio não especificou as validalções necessárias para aprovar o pagamento ou o estorno, então tomei a liberdade de criar as validações com base nos critérios que eu achei correto.
- Tomei também a liberdade de adiocionar o swagger como documentação.
- Em relação aos teste eu criei apenas para a classe service, pois eu acredito que os testes devem ser aplicadas de forma precisa, somente onde coração da lógica se encontra.



INSTRUÇÕES

Para garantir um correto funcionamento do banco de dados certifique-se de criar um arquivo "test.mv.db" na pasta de usuários.

- mvn clean install
- Executar a classe ToolsAplication como uma aplicação java



DOCUMENTAÇÃO

//Aqui você encontrará todos os endpoinst necessários para rodar a aplicação.
http://localhost:8080/swagger 

//documentação crua
http://localhost:8080/doc


Database - h2

http://localhost:8080/h2-console
