# InterdisziplinaeresProjekt

ToDo
- Fehlerseite erstellen bei fehlenden Rechten

Frameworks:
- Spring Boot
- Spring Security
- Spring JPA
- Thymeleaf
- Bootstrap
- Spring Web
- MySQL Connector
- Spring Actuator (Monitoring Framework)

Features:
- Login Maske (Ohne Login kommt man nicht auf die Seite)
Nach dem Login wird unterschieden ob man die Rolle "Manager" oder "Arbeiter" hat - Man wird automatisch auf verschiedene Seiten weitergeleitet.
Datenbankzugriffe werden per Spring JPA gemacht. Optionen dazu in der application.properties. Angepasst dazu müssen die TestRepository, TestService und TestServiceImpl. Viele Dinge dabei selbsterklärend - ansonsten fragt mich.