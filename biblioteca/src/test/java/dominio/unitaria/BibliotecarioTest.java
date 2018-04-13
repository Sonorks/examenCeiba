package dominio.unitaria;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import dominio.Bibliotecario;
import dominio.Libro;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;
import testdatabuilder.LibroTestDataBuilder;

public class BibliotecarioTest {

	@Test
	public void esPrestadoTest() {
		
		// arrange
		LibroTestDataBuilder libroTestDataBuilder = new LibroTestDataBuilder();
		
		Libro libro = libroTestDataBuilder.build(); 
		
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		
		when(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn())).thenReturn(libro);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		// act 
		
		boolean esPrestado =  bibliotecario.esPrestado(libro.getIsbn());
		
		//assert
		assertTrue(esPrestado);
	}
	
	@Test
	public void libroNoPrestadoTest() {
		
		// arrange
		LibroTestDataBuilder libroTestDataBuilder = new LibroTestDataBuilder();
		
		Libro libro = libroTestDataBuilder.build(); 
		
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		
		when(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn())).thenReturn(null);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		// act 
		boolean esPrestado =  bibliotecario.esPrestado(libro.getIsbn());
		
		//assert
		assertFalse(esPrestado);
	}
	
	@Test
	public void esPalindromoTest() {
		
		//arrange
		String isbn = "1021201";
		
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		//act
		boolean esPalindromo = bibliotecario.esPalindromo(isbn);
		
		//assert
		assertTrue(esPalindromo);
		
	}
	
	@Test
	public void noEsPalindromoTest() {
		
		//arrange
		String isbn = "101201";
		
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		//act
		boolean esPalindromo = bibliotecario.esPalindromo(isbn);
		
		//assert
		assertFalse(esPalindromo);
		
	}
	
	@Test
	public void sumaMasDe30Test() {
	
		//arrange
		String isbn = "9999";
		
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		//act
		boolean sumaMasDe30 = bibliotecario.sumaMasDe30(isbn);
		
		//assert
		assertTrue(sumaMasDe30);
	}
	
	@Test
	public void noSumaMasDe30Test() {
	
		//arrange
		String isbn = "999";
		
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		//act
		boolean sumaMasDe30 = bibliotecario.sumaMasDe30(isbn);
		
		//assert
		assertFalse(sumaMasDe30);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void calcularFechaEntregaMaximaTest() {
		
		//arrange 
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		//act
		Date fechaEntregaMaxima = bibliotecario.calcularFechaEntregaMaxima();
		Calendar calendar = Calendar.getInstance();
		Date fechaEsperada;
		if(calendar.get(Calendar.DAY_OF_WEEK)>1 && calendar.get(Calendar.DAY_OF_WEEK)<6) {
			calendar.add(Calendar.DAY_OF_MONTH, 16);
			fechaEsperada = calendar.getTime();
		} else {
			calendar.add(Calendar.DAY_OF_MONTH, 17);
			fechaEsperada = calendar.getTime();
		}

		//assert
		assertTrue(fechaEsperada.getDate() == fechaEntregaMaxima.getDate());
	}
}
