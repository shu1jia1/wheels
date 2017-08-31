#什么是lamada表达式
编程中提到的 lambda 表达式，通常是在需要一个函数，但是又不想费神去命名一个函数的场合下使用，也就是指匿名函数。

#详细解释lambda表达式:
要理解lambda表达式，首先要了解的是函数式接口（functional interface）。简单来说，函数式接口是只包含一个抽象方法的接口。比如Java标准库中的java.lang.Runnable和java.util.Comparator都是典型的函数式接口。对于函数式接口，除了可以使用Java中标准的方法来创建实现对象之外，还可以使用lambda表达式来创建实现对象。这可以在很大程度上简化代码的实现。在使用lambda表达式时，只需要提供形式参数和方法体。由于函数式接口只有一个抽象方法，所以通过lambda表达式声明的方法体就肯定是这个唯一的抽象方法的实现，而且形式参数的类型可以根据方法的类型声明进行自动推断。
```
public void runThread() {
    new Thread(new Runnable() {
        public void run() {
            System.out.println("Run!");
        }
    }).start();
}
-->
public void runThreadUseLambda() {
    new Thread(() -> {
        System.out.println("Run!");
    }).start();
}

```

#Lambda表达式的语法
基本语法:
(parameters) -> expression
或
(parameters) ->{ statements; }

例子:
```
// 1. 不需要参数,返回值为 5
() -> 5

// 2. 接收一个参数(数字类型),返回其2倍的值
x -> 2 * x

// 3. 接受2个参数(数字),并返回他们的差值
(x, y) -> x – y

// 4. 接收2个int型整数,返回他们的和
(int x, int y) -> x + y

// 5. 接受一个 string 对象,并在控制台打印,不返回任何值(看起来像是返回void)
(String s) -> System.out.print(s)
```
