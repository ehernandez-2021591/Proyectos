package User2;
public class Producto{
	private String nombreproducto;
	private int cantidad;
	private String categoria;
    private int precio;

    public Producto(){

    } public Producto(String nombreproducto,int cantidad,
    	String categoria,int precio){
    	this.nombreproducto=nombreproducto;
    	this.cantidad=cantidad;
    	this.categoria=categoria;
    	this.precio=precio;
    }
    public void setnombreproducto(String nombreproducto){
    	this.nombreproducto = nombreproducto;
    }
    public String getnombreproducto(){
    	return nombreproducto;
    }
    public void setcantidad(int cantidad){
    	this.cantidad=cantidad;
    }
    public int getcantidad(){
    	return cantidad;
    }
    public void setcategoria(String categoria){
    	this.categoria=categoria;
    }
    public String getcategoria(){
    	return categoria;
    }
    public void setprecio(int precio){
    	this.precio=precio;

    }public int getprecio(){
    	return precio;
    }







}