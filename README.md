# Minecraft Spawn Plugin

Minecraft Spawn to plugin, który pozwala graczom ustawić oraz teleportować się do wcześniej zdefiniowanego punktu spawnu. Plugin umożliwia łatwe zarządzanie teleportacją oraz dostosowanie wiadomości i dźwięków związanych z teleportacją.

## Funkcje
- **Ustawianie spawnu**: Użyj komendy, aby ustawić punkt spawnu, do którego gracze będą teleportowani.
- **Teleportacja na spawn**: Teleportuj graczy do ustawionego punktu spawnu za pomocą prostej komendy.
- **Przeładowanie konfiguracji**: Plugin pozwala na przeładowanie ustawień z pliku konfiguracyjnego bez konieczności restartu serwera.

## Użycie Komend

- `/setspawn`  
  **Opis:** Ustawia bieżące współrzędne gracza jako punkt spawnu.  
  **Przykład:** `/setspawn` – Ustawia punkt spawnu w aktualnej lokalizacji.

- `/spawn`  
  **Opis:** Teleportuje gracza do ustawionego punktu spawnu.  
  **Przykład:** `/spawn` – Teleportuje gracza na spawn.

- `/spawn reload`  
  **Opis:** Przeładowuje konfigurację pluginu, umożliwiając zastosowanie zmian z pliku konfiguracyjnego.

## Konfiguracja

Plugin automatycznie tworzy plik konfiguracyjny, w którym można dostosować:

- **Czas teleportacji**: Określ czas, po jakim gracz zostaje przeteleportowany.
- **Wiadomości**: Dostosuj wiadomości, które gracz widzi:
  - Kiedy teleportacja zostaje anulowana.
  - Kiedy teleportacja się rozpoczyna.
  - Kiedy teleportacja zakończy się sukcesem.
- **Dźwięki**: Ustaw dźwięki, które odtwarzane są w trakcie procesu teleportacji.

### Przykład konfiguracji (`config.yml`):

```yaml
counter:
  title: "&7Teleportacja nastąpi za: &f%time% sekund!"
  title_constant: "&c&lTeleportacja"
  time: 5
messages:
  reload: "&aKonfiguracja została przeładowana!"
  only_players: "&cPodana komenda jest dostępna tylko dla graczy!"
  no_permission: "&cNie masz uprawnień do używania tej komendy!"
  already_teleporting: "&cJesteś już w trakcie teleportacji!"
  spawn_not_set: "&cLokalizacja spawnu nie jest ustawiona! Ustaw ją za pomocą /setspawn."
  plugin_disabled: "&cPlugin jest wyłączony, nie można rozpocząć teleportacji."
  teleport_cancelled: "&cTeleportacja została przerwana!"
  teleport_success: "&aZostałeś przeteleportowany na spawn!"
sounds:
  teleport_countdown: BLOCK_NOTE_BLOCK_HAT
  teleport_success: ENTITY_PLAYER_LEVELUP
  teleport_cancelled: BLOCK_ANVIL_LAND
```

**Wymagania:**
- Serwer Minecraft (wersja 1.16+)
- Java 8+

**Autor:** [ScreamMaster1337](https://github.com/ScreamMaster1337)  
**Licencja:** MIT
