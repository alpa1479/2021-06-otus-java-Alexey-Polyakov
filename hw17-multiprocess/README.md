### Задание 17 - Многопроцессные приложения. ДЗ

1. Серверная часть.<br>
   сервер по запросу клиента генерирует последовательность чисел.<br>
   запрос от клиента содержит начальное значение (firstValue) и конечное(lastValue).<br>
   Раз в две секунды сервер генерирует новое значение и "стримит" его клиенту:<br>
   firstValue + 1<br>
   firstValue + 2<br>
   ...<br>
   lastValue<br>

2. Клиентская часть.<br>
   клиент отправдяет запрос серверу для получения последовательности чисел от 0 до 30.<br>
   клиент запускает цикл от 0 до 50.<br>
   раз в секунду выводит в консоль число (currentValue) по такой формуле:<br>
   currentValue = [currentValue] + [ПОСЛЕДНЕЕ число от сервера] + 1<br>
   начальное значение: currentValue = 0<br>
   Число, полученное от сервера должно учитываться только один раз.<br>
   Обратите внимание, сервер может вернуть несколько чисел, надо взять именно ПОСЛЕДНЕЕ.<br>

Должно получиться примерно так:<br>
currentValue:1<br>
число от сервера:2<br>
currentValue:4 <--- число от сервера учитываем только один раз<br>
currentValue:5 <--- тут число от сервера уже не учитывается.<br>
число от сервера:3<br>
currentValue:9<br>
currentValue:10<br>
new value:4<br>
currentValue:15<br>
currentValue:16<br>

Для коммуникации используйте gRPC.<br>
Клиент и сервер не обязательно разделять по модулям.<br>
Можно сделать один модуль с двумя main-классами для клиента и сервера.<br>

Пример лога работы клиента (new value - это значение полученное от сервера)<br>

21:44:04.782 [main] INFO ru.otus.numbers.client.NumbersClient - numbers Client is starting...<br>
21:44:04.932 [main] INFO ru.otus.numbers.client.NumbersClient - currentValue:1<br>
21:44:05.140 [grpc-default-executor-0] INFO r.o.n.client.ClientStreamObserver - new value:2<br>
21:44:05.933 [main] INFO ru.otus.numbers.client.NumbersClient - currentValue:4<br>
21:44:06.933 [main] INFO ru.otus.numbers.client.NumbersClient - currentValue:5<br>
21:44:07.113 [grpc-default-executor-0] INFO r.o.n.client.ClientStreamObserver - new value:3<br>
21:44:07.934 [main] INFO ru.otus.numbers.client.NumbersClient - currentValue:9<br>
21:44:08.934 [main] INFO ru.otus.numbers.client.NumbersClient - currentValue:10<br>
21:44:09.112 [grpc-default-executor-0] INFO r.o.n.client.ClientStreamObserver - new value:4<br>
21:44:09.935 [main] INFO ru.otus.numbers.client.NumbersClient - currentValue:15<br>
21:44:10.935 [main] INFO ru.otus.numbers.client.NumbersClient - currentValue:16<br>
21:44:11.113 [grpc-default-executor-0] INFO r.o.n.client.ClientStreamObserver - new value:5<br>
21:44:11.935 [main] INFO ru.otus.numbers.client.NumbersClient - currentValue:22<br>
21:44:12.936 [main] INFO ru.otus.numbers.client.NumbersClient - currentValue:23<br>
21:44:13.113 [grpc-default-executor-0] INFO r.o.n.client.ClientStreamObserver - new value:6<br>
21:44:13.936 [main] INFO ru.otus.numbers.client.NumbersClient - currentValue:30<br>
21:44:14.937 [main] INFO ru.otus.numbers.client.NumbersClient - currentValue:31<br>
21:44:15.114 [grpc-default-executor-0] INFO r.o.n.client.ClientStreamObserver - new value:7<br>
21:44:15.938 [main] INFO ru.otus.numbers.client.NumbersClient - currentValue:39<br>
21:44:16.938 [main] INFO ru.otus.numbers.client.NumbersClient - currentValue:40<br>
21:44:17.113 [grpc-default-executor-0] INFO r.o.n.client.ClientStreamObserver - new value:8<br>
21:44:17.939 [main] INFO ru.otus.numbers.client.NumbersClient - currentValue:49<br>
21:44:18.939 [main] INFO ru.otus.numbers.client.NumbersClient - currentValue:50<br>
21:44:19.113 [grpc-default-executor-0] INFO r.o.n.client.ClientStreamObserver - new value:9<br>
21:44:19.940 [main] INFO ru.otus.numbers.client.NumbersClient - currentValue:60<br>
21:44:20.940 [main] INFO ru.otus.numbers.client.NumbersClient - currentValue:61<br>
21:44:21.114 [grpc-default-executor-0] INFO r.o.n.client.ClientStreamObserver - new value:10<br>
21:44:21.119 [grpc-default-executor-0] INFO r.o.n.client.ClientStreamObserver - request completed<br>