# Seminario SQL Injection
Questa repository contiene tutto il materiale realizzato per il seminario vertente l'**SQL Injection**.
_Progetto sviluppato nell'ambito dell'insegnamento di [BASI DI DATI E SISTEMI INFORMATIVI](https://poliba.coursecatalogue.cineca.it/af/2025?corso=LT17&annoOrdinamento=2022&pds=LT17-26&coorte=2024&ad=2185&piano=774&lingua=ita&sede=1005&linguaCC=it) presso il Politecnico di Bari_.
## Cosa puoi trovare in questa repository
- Le slides e le immagini presentate durante il seminario ([vai lì](https://github.com/emaxlap-rs/seminario-sql-injection/tree/main/materiale))
- Il progetto del web server utilizzato in aula per le dimostrazioni pratiche ([vai lì](https://github.com/emaxlap-rs/seminario-sql-injection/tree/main/sorgente/vulnerable-site))
- Il codice SQL per costruire il database di prova ([vai lì](https://github.com/emaxlap-rs/seminario-sql-injection/tree/main/sorgente/SQL))
## Tip
Se ti interessa solo una cartella della repository nello specifico, puoi utilizzare strumenti come [Git Folder Downloader](https://gitfolderdownloader.github.io/), al posto di clonare l'intera repository.
## Come eseguire il webserver
1. assicurati di avere installato sulla macchina [IntelliJ IDEA](https://www.jetbrains.com/idea/)
2. apri la directory `sorgente/vulnerable-site` con l'IDE
3. installa il JDK `openjdk-26.0.1` (apparirà un popup)
4. avvia PostgreSQL
5. esegui gli script contenuti nella directory `sorgente/SQL`
6. assicurati che l'indirizzo, lo username e la password contenute nella classe `Database.java` coincidano con quelle della tua installazione di PostgreSQL
7. compila ed esegui il server web dall'IDE