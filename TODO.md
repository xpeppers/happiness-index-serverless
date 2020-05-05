## TODO

* [ ] `WIP=>` :rocket: estendere la chiamata di add e l'oggetto vote in modo che accetti autore, data e sede

#### HTTP

`POST /votes/<vote>`

`POST /votes/`
```json= 
{
    "vote": <vote>,
    "user": "<user-id>",
    "location": "<location-id>"
}
```

#### on S3:

```
 ...
 <data>,<vote>,<user-id>,<location-id>\n
 <data>,<vote>,<user-id>,<location-id>\n
 <data>,<vote>,<user-id>,<location-id>\n
 ...
```

- ~~modificare la getVotes in mode che torni tutti i dati del voto, non solo il numero e la data~~
* la date di default di `UserVote` non sembra funzionare quando devo deserializzare un JSON che non la contiene... (provare Jackson?)
* in `exception(Throwable::class.java) { _, _, t -> t.printStackTrace() }` dovremmo rilanciare l'errore?
* output dei test è inutilmente verboso (awssdk logga troppo!)
* ~~`VotesOnS3.all()` più idiomatica?~~
* ~~rimuovere la all vecchia e sostituirla con la all2~~
* salvare anche location e user
* ~~deserializzare correttamente la data~~
* arricchire AT HappinessVoteAcceptanceTest
- ~~modificare `VotesOnS3` in modo che persista anche user, location e date~~
- ???: il voto è sempre numerico?

* perché non passa il test di integrazione su S3 se lo lancio da IDEA?
* come posso fare per lanciare i test da IDEA in modo che prenda l'utente xpeppers-develop con STS?

* PRIVACY: ma lo userid non viola la privacy? 
* [ ] :rocket: aggungere query nella chiamata getvotes: filtrare per sede e ottenere i voti dati in un determinato span temporale
* [ ] :rocket: report sull'happiness delle persone: aggiungere un sito statico (usando bucket s3?) che mostra rudimentalmente i voti querati
* [ ] sostituire il sistema di persistenza con qualcosa in grado di gestire la concorrenza (dynamo? fauna?)
    * [ ] spike su VotesOnDynamoDB
    * [ ] spike su VotesOnFaunaDB
* [ ] :rocket: l'utente puo' aggiungere un suo commento sulla giornata (con una nuova chiamata?)
* [ ] :rocket: aggiungere constaint di un solo voto al giorno per autore e dopo le 18
* [ ] :rocket: sostituire il voto numerico con le faccine
* [ ] :rocket: migliorare la grafica del sito con grafici, datapicker...
* ispirarci a timetableless: https://github.com/txpeppers/timetableless
  * l'uso delle variabili d'ambiente impostate nel serverlessyml 
  * [x] ~~la creazione del bucket~~
* [ ] rimuovere duplicazione tra applicazione e configurazione yaml (con variabili d'ambiente?)
* [ ] suddividere ambiente dev da ambiente prod (vedi anche come hanno fatto quelli di timetableless)

* [ ] integrare le API con un bot Slack / Telegram o anche via CLI
* 
* CI: costruire uno script di build (unit, integration, deploy, acceptance, deploy prod)
    - smoke test: `curl -v  https://g49lpxwuhd.execute-api.eu-west-1.amazonaws.com/dev/happiness/votes | jq`
---

* estrarre nome della key dove sono salvati i nomi (classe di configurazione?)
* dobbiamo resettare il bucket ogni volta che lanciamo la suite di test?
* SPIKE: fare un porting su Kotless https://github.com/JetBrains/kotless
* aggiornando alla ultima versione di AWS-SDK l'app non funziona per problemi di credenziali 2.12.0 (2.11.*)
* ~~mio account vs tuo account~~
* ~~il bucket deve già esistere o meno?~~


### useful docs

* AWS SDK for Java and AWS Credentials: https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/credentials.html

### dubbi e domande

#### Come autenticare una rotta?

=> AWS Cognito
=> OAuth di Google

#### Dynamo - potrebbe andare bene per salvare gli "happiness status"? lezioni imparate su dynamo? 

`primary key` (solo uguaglianza) upfront 
`secondary key` (compare, ma solo dopo la primary)

"time series" supportata da dynamodb

+1 se puoi partizionare i tuoi dati in maniera deterministica (upfront)

* provare FaunaDB: https://fauna.com/ ?

primary key = month
secondary key = day

oppure

primary key = user
secondary key = timestamp

---

### User stories
```
Come utente 
Posso esprimere un feedback sulla mia giornata 
Chiamando un endpoint HTTP come questo

POST /happiness/{1-4}
```

```
201 CREATED
body: "Thanks for voting :D"
```

---

```
come utente 
voglio poter sapere come è andata la giornata degli altri utenti 
chiamando un endpoin come questo:

GET /happiness/{date}
```

```json
{
	"1": 3,
	"2": 0,
	"3": 2,
	"4": 2
}
```

* come utente assieme al feedback numerico sulla giornata posso anche mandare una nota testuale 
* registrazione
* localizzazione (per città)
* come utente voglio poter sapere come è andata la giornata degli altri utenti suddivisa per città
* come utente voglio poter sapere qual'è l'HI degli altri utenti in un intervallo temporale di date
