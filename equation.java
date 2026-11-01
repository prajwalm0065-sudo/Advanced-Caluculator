import java.util.Scanner;
class Equation{
    int n;
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter number of unknowns(2,3,4): ")
    n = sc.nextInt();
    int a1, a2, a3, a4, b1, b2, b3, b4,c1, c2, c3, c4;
    switch(n){
        case 2:
            System.out.println("Equations:(a1x+b1y=c1),(a2x+b2y=c2)")
            System.out.println("Enter the value of (a1, b1, c1, and a2, b2, c2):");
            a1 = System.out.print();
            a2 = System.out.print();
            b1 = System.out.print();
            b2 = System.out.print();
            c1 = System.out.print();
            c2 = System.out.print();
            Calculation.twouk(a1, b1, c1, a2, b2, c2);
            break;
        case 3:
            System.out.println("Equations:(a1x+b1y=c1),(a2x+b2y=c2),(a3x+b3y=c3)")
            System.out.println("Enter the value of (a1, b1, c1, and a2, b2, c2 and a3, b3, c3):");
            a1 = System.out.print();
            a2 = System.out.print();
            a3 = System.out.print();
            b1 = System.out.print();
            b2 = System.out.print();
            b3 = System.out.print();
            c1 = System.out.print();
            c2 = System.out.print();
            c3 = System.out.print();
            Calculation.threeuk(a1, a2, a3, b1, b2, b3, c1, c2, c3);
            break;
        case 4:
            System.out.println("Equations:(a1x+b1y=c1),(a2x+b2y=c2),(a3x+b3y=c3),(a4x+b4y=c4)")
            System.out.println("Enter the value of (a1, b1, c1, and a2, b2, c2 and a3, b3, c3):");
            a1 = System.out.print();
            a2 = System.out.print();
            a3 = System.out.print();
            a4 = System.out.print();
            b1 = System.out.print();
            b2 = System.out.print();
            b3 = System.out.print();
            b4 = System.out.print();
            c1 = System.out.print();
            c2 = System.out.print();
            c3 = System.out.print();
            c4 = System.out.print();
            Calculation.fouruk();
            break;
        default:
            System.out.println("Input values 2, 3 and 4 only");
    }

}
class Calculation{
    int twouk(int a1, int a2, int x2, int y2){

    }

}
