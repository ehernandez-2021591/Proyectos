import User2.*;
import java.util.Scanner;
import java.util.ArrayList;
public class Wichos{
	public static void main(String[] args) {
    String nombre;
    int opcion;
    int opcion1;
		Scanner leer = new Scanner(System.in);
		ArrayList<Registro> listausers=new ArrayList<>(10);
    ArrayList<Producto> listaproductos=new ArrayList<>();
		listausers.add(new Registro("luis Peres","wichos342","wichos13242".toUpperCase()));
		listausers.add(new Registro("Maria Ruiz","MarY1989.hotmail","Mary465".toUpperCase()));
		System.out.println("Como desea Ingresar:\n1.Admin\n2.Usuario:\n3.Salir");
      opcion=leer.nextInt();
    boolean salir = false;
			
		
		
        while(!salir){
        	
        	System.out.println("1.Ingresar Usuario");
        	System.out.println("2.Ver lista de usuarios");
        	System.out.println("3.Ver Inventario");
        	System.out.println("4.Organizar Inventario");
        	System.out.println("5.Facturar");
        	System.out.println("0.Salir");
        	opcion=leer.nextInt();
            if (opcion==5) {
            }
		 switch(opcion) {
                case 1:{
                    Registro registro = new Registro();
                    System.out.println("Ingrese nombre del usuario:");
                    leer.nextLine();
                    registro.setnombrecompleto(leer.nextLine());
                    System.out.println("Ingrese un correo electronico: ");
                    registro.setcorreoelectronico(leer.nextLine());
                    System.out.println("Ingrese password: ");
                    registro.setpassword(leer.nextLine());
                   

                
                    listausers.add(registro);
                    
                    
                    
                    System.out.println("Usuario Creado: ");
                    System.out.println("Nombre " + registro.getcorreoelectronico());
                    System.out.println("Correo " + registro.getnombrecompleto());
                    System.out.println("Password " + registro.getpassword());
                    
                   } break;
                case 2: 
                for(int i = 0; i < listausers.size(); i++){ 
                System.out.println("Usuario: " + (i + 1));
                Registro user =  listausers.get(i); 
                       
                System.out.println("Nombre: " + user.getnombrecompleto());
                System.out.println("Correo Electronico: " + user.getcorreoelectronico());
                System.out.println("Password: " + user.getpassword());
                       
                    }break;
                   case 3:
                    for(int i = 0;i<listaproductos.size(); i++){ 
                    System.out.println("Producto: " + (i + 1));
                    Producto produc =  listaproductos.get(i); 
                       
                    System.out.println("---------Categoria: " + produc.getcategoria());
                    System.out.println("---------Nombre producto: " + produc.getnombreproducto());
                    System.out.println("---------Cantidad: " + produc.getcantidad());
                    System.out.println("---------Precio:"+produc.getprecio());
                   }break;
                  
                   case 4 :
                   System.out.println("1.Agregar un producto al inventario\n2.Eliminar producto\n3.Modificar producto");
                    opcion1=leer.nextInt();
                   switch(opcion1){
                   case 1:
                    Producto agregar = new Producto();
                    System.out.println("Ingrese el nombre del producto");
                    leer.nextLine();
                    agregar.setnombreproducto(leer.nextLine());
                    System.out.println("Ingrese la cantidad que estara disponible");
                    agregar.setcantidad(Integer.parseInt(leer.nextLine()));
                    System.out.println("Ingrese la categoria a la que pertenece el producto ejemplo:-Bebidas-, -Entrada-, -Plato fuerte-, o -Postre-");
                    agregar.setcategoria(leer.nextLine());
                    System.out.println("Ingrese el precio que tendra el producto");
                    agregar.setprecio(Integer.parseInt(leer.nextLine()));
                    listaproductos.add(agregar);
                    System.out.println("Producto creado y agregado");
                    System.out.println("----------Categoria:--------" + agregar.getcategoria());
                    System.out.println("----------Nombre :--------"+ agregar.getnombreproducto());
                    System.out.println("----------Cantidad:--------" + agregar.getcantidad());
                    System.out.println("----------Precio:--------- Q"+agregar.getprecio());
                   break;
                   case 2 :
                    System.out.println("Ingrese el nombre del producto a eliminar:");
                    String nombreproduc = leer.nextLine();
                    nombreproduc=leer.nextLine();
                    for (int i = 0; i < listaproductos.size(); i++) {
                    Producto produc = listaproductos.get(i);
                    if (nombreproduc.equals(produc.getnombreproducto())) {
                    listaproductos.remove(i);
                    System.out.println("Producto eliminado");
                        }}
                    break;
                    case 3: System.out.println(" Ingrese el nombre del Producto a modificar:");
                            nombreproduc = leer.nextLine();
                            nombreproduc=leer.nextLine();
                            for (int i = 0; i < listaproductos.size(); i++) {
                            Producto produc = listaproductos.get(i);
                            if (nombreproduc.equals(produc.getnombreproducto())) {
                            System.out.println("Categoria: " + produc.getcategoria());
                            System.out.println("Cantidad: " + produc.getcantidad());
                            System.out.println("Precio"+ produc.getprecio());
                            System.out.println("Ingrese una nueva categoria: ");
                            String category = leer.nextLine();
                            System.out.println("Ingrese nueva cantidad: ");
                            int cuantity = leer.nextInt();
                            System.out.println("Ingrese el nuevo precio");
                            int price = leer.nextInt();;
                            System.out.println("Producto actualizado");
                            
                            produc.setcategoria(category);
                            produc.setcantidad(cuantity);
                            produc.setprecio(price);
                            
                       } }}break;
                      
                   case 5:
                   System.out.println("Ingrese el nombre del cliente:");
                   leer.nextLine();
                   String clienten=leer.nextLine();
                   System.out.println("Ingrese el Nit del cliente:");
                   int nint=leer.nextInt();
                   System.out.println("Cuantos productos va a agregar a la factura");
                   int product=leer.nextInt();
                   for (int c=0;c<product ;c++ ) {
                   System.out.println("Agregue el producto");
                   leer.nextLine();
                   String name=leer.nextLine();
                   System.out.println("Agregue la cantidad");
                   int cant=leer.nextInt();

                   for (int a = 0;  listaproductos.size()>a; a++) {
                   Producto produc = listaproductos.get(a);
                   if (name.equals(produc.getnombreproducto())) {
                   System.out.println("------------Wichos S.A------------");
                   System.out.println("---------6ta Av 16calle Z.1-------");
                   System.out.println("Nombre del cliente:"+clienten);
                   System.out.println("Nit del cliente:"+nint);
                   System.out.println("Producto-----------Total ");
                   System.out.println(produc.getnombreproducto()    +cant*produc.getprecio());
                      System.out.println("Total a pagar: Q");  
                
                  

                    
                            
          }  }}  break;
                    
                
                   case 0 : 
                   salir=true;

                   System.out.println("Terminando la ejecucuion del programa");
                   break;
                   default:
                   System.out.println("Ingrese una opcion que sea correcta!!!!!!!");
                     
            }       }
    }


}