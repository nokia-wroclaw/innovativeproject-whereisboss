Where is my boss?
=============

Where is my boss? Projekt stworzony w ramach Nokia Innovative Projects 2014.

O projekcie
-------

Na codzień jesteśmy otoczeni całą masą sygnałów radiowych. Niektóre można łatwo odebrać na długich dystansach (np. radio FM), a niektóre stworzono do komunikacji na bliskie odległości (np. NFC).
Wi-Fi ma zasięg około 60 metrów. Wszystkie urządzenia które mogą odbierać sygnał Wi-Fi mogą obliczyć siłę sygnału (<b>RSSI - Recieved Signal Strength Indication</b>).
RSSI pozwala określić jak daleko znajdujemy się od źródła sygnału. Jeżeli jesteśmy w zasięgu wielu sieci Wi-Fi możemy określić naszą pozycję bazując na RSSI z wielu różnych źródeł.

Projekt składa się z dwóch części:
* [Serwis internetowy] (https://whereisboss.herokuapp.com/) - strona internetowa razem z serwerem
* Aplikacja dla systemu Android 

Aplikacja działa w dwóch trybach:
- Skanowanie - zbierane są dane o widocznych Access Pointach (adresy MAC, nazwę sieci, siłę sygnału), przypisuje dane do wcześniej określonego pomieszczenia/obszaru i wysyła te dane na serwer gdzie są one zapisywane do bazy danych
- Raportowanie - aplikacja co kilka minut wysyła dane o widocznych Access Pointach na serwer, gdzie następuje analiza otrzymanych danych za pomocą danych zebranych podczas skanowania i określana jest pozycja użytkownika
 
Funkcjonalność
=============
Strona internetowa pozwala na:
-------
* rejestrację użytkownika
* stworzenie grup i zarządzanie nimi
* stworzenie map budynków w których chcemy określać pozycję użytkowników
  - wysłanie na stronę mapy pięter w postaci pdf/jpg 
  - narysowanie pokoi/obszarów za pomocą prostego interfejsu
* wyszukiwanie użytkowników z grupy użytkownika

Aplikacja Android pozwala na: 
-------
* skanowanie poszczególnych pokoi - wymagane jest wcześniejesze stworzenie map budynków na stronie
* raportowanie pozycji użytkownika - wykorzystuje dane zebrane podczas skanowania do określenia przybliżonej lokalizacji użytkownika
* wyszukiwanie użytkowników z grupy użytkownika

Technologie użyte w projekcie:
=============
Serwer
-------
* Hosting: [heroku.com] (https://www.heroku.com/)
* Node.js
* [AWS Amazon s3](http://aws.amazon.com/s3/) - przechowywanie plików map
* [Socket.io](http://socket.io/) - komunikacja serwer-strona i serwer-aplikacja jest oparta głównie na socketach
* [Monk](https://github.com/Automattic/monk) - ułatwiona komunikacja z bazą danych
* [MongoDB](http://www.mongodb.org/) - baza danych NoSQL
* [express.js](http://expressjs.com/) - tworzenie sesji użytkownika
* [MongoStore](https://github.com/kcbanner/connect-mongo) - przechowywanie sesji użytkownika

Strona internetowa 
-------
* JavaScript 
* [Foundation framework](http://foundation.zurb.com/)
* [JQuery](http://jquery.com/)
* [Socket.io] (http://socket.io/)
* [HTML5 Canvas](http://www.w3schools.com/html/html5_canvas.asp) - rysowanie map budynków

Aplikacja Android
--------
* Java
* [nkzawa socket.io](https://github.com/nkzawa/socket.io-client.java)
