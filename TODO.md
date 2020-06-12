## TODO

### Continuous Itegration

- [ ] CI: costruire uno script di build (unit, integration, deploy, acceptance, deploy prod)
  - [x] separare gli ambienti: dev preprod prod
  - [x] separare lanco dei test per ambiente 
  - [x] usare bucket separati per ambiente
  - [ ] copiare il bucket di prod negli ambienti preliminari(?)
  - [ ] come gestiamo i test nella produzione (non possiamo resettare i dat di prod)? 
- [x] GitHub Actions
  - [ ] account di CI
    - [x] creare account con tutti i permessi per la CI
    - [ ] limitare i permessi dell'account di CI con 'access advisor' o altri tool
  - [x] configurare build.gradle con i diversi stage
  - [x] creare script che crea i tag di success build
  - [x] configurare ci.yaml per portare in prod i tag release
-  [ ] smoke test: `curl -v  https://g49lpxwuhd.execute-api.eu-west-1.amazonaws.com/dev/happiness/votes | jq`
    

### Funzionalita'

- [ ] [DOING] :rocket: report sull'happiness delle persone: aggiungere un sito statico (usando bucket s3?) che mostra rudimentalmente i voti querati
- [ ] :rocket: migliorare la grafica del sito con grafici, datapicker...
- [ ] :rocket: l'utente puo' aggiungere un suo commento sulla giornata (con una nuova chiamata?)
- [ ] :rocket: aggiungere constraint di un solo voto al giorno per autore e dopo le 18
- [ ] :rocket: sostituire il voto numerico con le faccine

### Infrastruttura

- [ ] sostituire il sistema di persistenza con qualcosa in grado di gestire la concorrenza (dynamo? fauna?)
    - [ ] spike su VotesOnFaunaDB
    - [ ] spike su VotesOnDynamoDB
- [ ] integrare le API con un bot Slack / Telegram o anche via CLI

### Minor improvements

- [ ] output dei test è inutilmente verboso (awssdk logga troppo!)
- [ ] come posso fare per lanciare i test da IDEA in modo che prenda l'utente xpeppers-develop con STS?
- [x] rimuovere duplicazione tra applicazione e configurazione yaml (con variabili d'ambiente?)


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

---

### Dubbi, perplessita', note
- PRIVACY: ma lo userid non viola la privacy? 
- estrarre nome della key dove sono salvati i nomi (classe di configurazione?)
- dobbiamo resettare il bucket ogni volta che lanciamo la suite di test?
- SPIKE: fare un porting su Kotless https://github.com/JetBrains/kotless
- aggiornando alla ultima versione di AWS-SDK l'app non funziona per problemi di credenziali 2.12.0 (2.11.*)


### Useful docs

- AWS SDK for Java and AWS Credentials: https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/credentials.html

#### Come autenticare una rotta?

- AWS Cognito
- OAuth di Google

#### Dynamo - potrebbe andare bene per salvare gli "happiness status"? lezioni imparate su dynamo? 
```
`primary key` (solo uguaglianza) upfront 
`secondary key` (compare, ma solo dopo la primary)

"time series" supportata da dynamodb

+1 se puoi partizionare i tuoi dati in maniera deterministica (upfront)

primary key = month
secondary key = day

oppure

primary key = user
secondary key = timestamp
```


