package dominio;

import java.util.Calendar;
import java.util.Date;

import dominio.excepcion.PrestamoException;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;

public class Bibliotecario {

	public static final String EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE = "El libro no se encuentra disponible";
	public static final String EL_LIBRO_SOLO_SE_USA_EN_BIBLIOTECA = "los libros palíndromos"
			+ " solo se pueden usar en la biblioteca";
	
	private RepositorioLibro repositorioLibro;
	private RepositorioPrestamo repositorioPrestamo;

	public Bibliotecario(RepositorioLibro repositorioLibro, RepositorioPrestamo repositorioPrestamo) {
		this.repositorioLibro = repositorioLibro;
		this.repositorioPrestamo = repositorioPrestamo;

	}

	public void prestar(String isbn, String nombreUsuario) {
		Prestamo nuevoPrestamo;
		Libro libro;
		Libro prestado = repositorioPrestamo.obtenerLibroPrestadoPorIsbn(isbn);
		Date fechaSolicitud = new Date();
		Date fechaEntregaMaxima = null;
		if(prestado==null) {
			if(!esPalindromo(isbn)) {
				libro = repositorioLibro.obtenerPorIsbn(isbn);
				if(sumaMasDe30(isbn)) {
					fechaEntregaMaxima = calcularFechaEntregaMaxima(fechaSolicitud);;
					nuevoPrestamo = new Prestamo(fechaSolicitud, libro, fechaEntregaMaxima, nombreUsuario);
					repositorioPrestamo.agregar(nuevoPrestamo);
				} else {
					nuevoPrestamo = new Prestamo(fechaSolicitud, libro, fechaEntregaMaxima, nombreUsuario);
					repositorioPrestamo.agregar(nuevoPrestamo);
				}
			} else {
				throw new PrestamoException(Bibliotecario.EL_LIBRO_SOLO_SE_USA_EN_BIBLIOTECA);
			}
		} else {
			throw new PrestamoException(Bibliotecario.EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE);
		}
	}

	public boolean esPrestado(String isbn) {
		Libro libro = repositorioPrestamo.obtenerLibroPrestadoPorIsbn(isbn);
		return (libro!=null);
	}
	
	public boolean esPalindromo(String isbn) {
		int n;
		if(isbn != null) {
			n = isbn.length();
		} else {
			return false;
		}
		for (int i = 0; i < (n/2); i++) {
			if (isbn.charAt(i) != isbn.charAt(n -i -1)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean sumaMasDe30(String isbn) {
		int n;
		int valorActual;
		int sumatoria = 0;
		if(isbn != null) {
			n = isbn.length();
		} else {
			return false;
		}
		for(int i = 0; i < n; i++) {
			if(Character.isDigit(isbn.charAt(i))) {
				valorActual = Character.getNumericValue(isbn.charAt(i));
				sumatoria += valorActual;
			}
		}
		return (sumatoria > 30);
	}
	
	/**
	 * Calcula la fecha de entrega máxima con el criterio de que si el prestamo se
	 * realiza un Domingo, un viernes o un sábado el plazo es de 17 días calendario,
	 * pero si el prestamo se hace un Lunes,Martes,Miercoles o Jueves el plazo que se
	 * da es de 16 días calendario. Esto por la cantidad de domingos que abarca el tramo
	 * de los 15 días hábiles.
	 * @param fechaSolicitud
	 * @return fechaEntregaMaxima
	 */
	public Date calcularFechaEntregaMaxima(Date fechaSolicitud) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fechaSolicitud);
		int hoy = calendar.get(Calendar.DAY_OF_WEEK);
		if(hoy > 1 && hoy < 6) {
			calendar.add(Calendar.DAY_OF_MONTH, 16);
		} else {
			calendar.add(Calendar.DAY_OF_MONTH, 17);
		}
		return calendar.getTime();
	}

}
