### Задание 5 - Байт код, class-loader, инструментация, asm. ДЗ

Разработайте такой функционал:<br>
метод класса можно пометить самодельной аннотацией @Log, например, так:<br>

class TestLogging {<br>
  @Log<br>
   public void calculation(int param) {};<br>
}<br>

При вызове этого метода "автомагически" в консоль должны логироваться значения параметров.<br>
Например так.<br>

class Demo {<br>
  public void action() {<br>
    new TestLogging().calculation(6); <br>
  }<br>
}<br>

В консоле дожно быть:<br>
executed method: calculation, param: 6<br>

Обратите внимание: явного вызова логирования быть не должно.<br>

Учтите, что аннотацию можно поставить, например, на такие методы:<br>
public void calculation(int param1) <br>
public void calculation(int param1, int param2) <br>
public void calculation(int param1, int param2, String param3)