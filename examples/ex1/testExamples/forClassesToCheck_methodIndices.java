package Ex1.tests;

//class Main {
//    public static void main(String[] args) {
//        System.out.println(6);
//    }
//}

class E{
    int field1;

}

class A extends E{
    int field2;
    public int foo(int a){
        return a+a;
    }
    public int foo2(int a){
        return a+a;
    }
}

class F extends E{
    public int foo(int a){
        return a+a;
    }

}

class B extends A{
    public int foo(int a){
        return a+a;
    }

}

class C extends A{
    public int foo(int a){
        return a+a;
    }
    public int foo3(int a){
        return a+a;
    }


}

class D extends B{
    public int foo4(int a){
        return a+a;
    }

}