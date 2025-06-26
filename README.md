# Integracja Allegro i SubiektGT ze Sferą - zamówienia, faktury sprzedaży, paragony i produkty

## Wstęp

### Wprowadzenie

Repozytorium dotyczy aplikacji umożliwiającej przede wszystkim pobranie zamówień z Allegro i późniejsze zapisanie ich w Subiekcie w postaci faktur sprzedaży albo 
zwykłych paragonów. W aplikacji można zaznaczyć wiele zamówień pobranych z Allegro i następnie jednym kliknięciem dodawane są faktury albo zwykłe paragony. 
Dodatkowo po utworzeniu zamówień Allegro w Subiekcie można jeszcze w aplikacji Integracja dla wybranych zamówień w Allegro przypisać dokumenty sprzedaży otrzymane
 w Subiekt GT. Aplikacja wspiera oferty z Allegro w wielu konfiguracjach:
* Jeden produkt,
* Zestaw jednego produktu,
* Zestaw wielu produktów.

Dodatkowo w aplikacji Integracja można przeglądać oferty obecne w Allegro wraz z informacjami, czy znaleziono analogiczny produkt w Subiekcie. Również w razie
potrzeby, w aplikacji można zapisac oferty pobrane z Allegro do pliku i następnie można te produkty szybko dodać do Subiekt GT.

### Sfera

Aplikacja Integracja korzysta z dodatku Sfera do Subiekt GT i dzięki temu można szybko dodać pożądane dokumenty do Subiekta. Wtyczka Sfera umożliwia uruchomienie 
w Subiekcie kanału komunikacyjnego, poprzez który aplikacje mogą się w sposób bezpośredni komunikować z Subiektem. Technicznie otwierane jest API typu COM, które 
jest technologią najczęściej używaną w C#. Dodatek mocno usprawnia integrację, ale niestety mechanizm ten jest dodatkowo płatny, lecz chociaż zakup jest 
jednorazowy.

Jak wspomniałem Sfera jest raczej związana z językiem C#. Ja niestety nie znam za bardzo tego języka i ograniczyłem się do Javy. Dlatego wykorzystałem nakładkę 
na Sfere napisaną przez kogoś innego w php. Wtyczka jest zamieszczona na https://github.com/Lukegpl/api-subiekt-gt/wiki/API-Dokumentacja-v.-1.0
[repozytorium GitHub Lukegpl](https://github.com/Lukegpl/api-subiekt-gt?tab=readme-ov-file).
Nakładka ta okazała się całkiem dobra. Przede wszystkim używa REST i JSON oraz jest dosyć dobrze 
[udokomentowana](https://github.com/Lukegpl/api-subiekt-gt?tab=readme-ov-file). Są szczegółowe informacje o realizowanych funkcjach np. jakie dane są wymagane 
dla poszczególnych endpointów oraz co prawdopodobnie będzie zwrócone. Niestety musiałem też trochę przerobić kod tej nakładki, aby spełniałamoje potrzeby 
np. wprowadziłem tworzenie klienta jednorazowego przy zapisywaniu faktur sprzedaży i zwykłych paragonów, a we wcześniejszej wersji nakładki,
klienci musieli już istnieć w Subiekcie przed dodaniem nowych obiektów. Dodatkowo w nakładce dodanie faktury sprzedaży musiało być poprzedzone dodaniem 
zamówienia od klienta. Również to zmieniłem i u mnie mogą być odrazu dodane dokumenty do Subiekta.

Aplikacja jest wykorzystywana produkcyjnie przez mojego znajomego zajmującego się sprzedażą w Allegro i zarządzaniem finansami i magazynem w Subiekt GT. Na chwile
obecną jest zadowolony i w razie czego wprowadzam drobne poprawki.

Ze względów bezpieczeństwa aplikacja ta zamieszczona na tym repozytorium operuje na środowisku testowym Allegro Sandbox, ale również mam wersję aplikacji
przeznaczoną dla standardowego środowiska Allegro (produkcyjnego), która jest aktualnie wykorzystywana przez mojego znajomego. Wersja produkcyjna jest
 niepubliczna. W razie potrzeby skorzystania z tej aplikacji na poważnie proszę napisać na mój adres e-mail kamdyw@wp.pl i chętnie przekażę wersję produkcyjną.

### Technologie

* Java,
* Swing,
* Maven,
* REST
* JSON,
* Sfera
* COM,
* Php
* JWT,
* OAuth 2.0,
* Git.

## Krótki poradnik

