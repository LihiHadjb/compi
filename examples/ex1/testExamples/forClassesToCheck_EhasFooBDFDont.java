package Ex1.tests;

//class Main {
//    public static void main(String[] args) {
//        System.out.println(6);
//    }
//}

class E{
    int field1;
    public int foo(int a){
        return a+a;
    }

}

class A extends E{
    int field2;
    public int foo(int a){
        return a+a;
    }
}

class F extends E{


}

class B extends A{


}

class C extends A{
    public int foo(int a){
        return a+a;
    }

}

class D extends B{


}