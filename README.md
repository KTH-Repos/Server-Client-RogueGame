# INET

## Protokoll Beskrivning

Protokollet som används för att överföra data är TCP. Både klient och server kommunicerar med varandra genom deras sockets inputstream och outputstream. Klientens inputStream är kopplad med Serverns outputStream och Klientens outputStream är kopplad med Serverns inputStream. De två olika strömmar av data är oberoende av varandra och är samtidigt tillgängliga för både server och klient, vilket betyder att data kan skickas utan begränsning från båda ändarna.  

Servern skapar först ServerSocket på en viss PORT och väntar tills en klient ansluter sig via porten. accept() är blockerande och väntar tills en anslutning skapas med en klient. På klients sida skapas en Socket också som sedan kan koppla sig med Serverns socket med giltiga IP och portnummer

I min implementation så är binär data som skickas mellan servern och klienten. Det som framförallt överförs mellan de två är position av spelare. Till exempel har jag gjort följande metod för att skicka position från server to klient
 ```java 
 public void sendPosition() {
        this.output.println("POS" + "," + position[0] + "," + position[1]);
        this.output.flush();
    }
```
output är en PrintWriter objekt och tar emot strängar och överför dem som binär data till en klient. Metodens motparten i klient är:
```java
 private static void setPlayerPosition(Map map, String pos) {
        String[] args = pos.split(",");
        int x = Integer.parseInt(args[1]);
        int y = Integer.parseInt(args[2]);
        map.setString(" ", map.getPlayerX(), map.getPlayerY());
        map.p1.setPosX(x);
        map.p1.setPosy(y);
        map.setString("@", x, y);
        out.println("NPOS" + "," + map.getPlayerX() + "," + map.getPlayerY());
        map.printMap();
    }
```
Denna metod anropas varje gång en spelare förflyttar sig i spelplanet och klienten vill sedan skicka den nya positionen till servern. 