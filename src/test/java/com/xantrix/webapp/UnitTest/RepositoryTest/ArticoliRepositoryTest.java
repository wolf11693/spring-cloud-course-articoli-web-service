package com.xantrix.webapp.UnitTest.RepositoryTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.xantrix.webapp.Application;
import com.xantrix.webapp.entity.Articoli;
import com.xantrix.webapp.entity.Articolo;
import com.xantrix.webapp.entity.Barcode;
import com.xantrix.webapp.entity.FamigliaAssortimento;
import com.xantrix.webapp.entity.Ingrediente;
import com.xantrix.webapp.repository.ArticoliRepository;

@SpringBootTest()
@ContextConfiguration(classes = Application.class)
@TestMethodOrder(OrderAnnotation.class)
public class ArticoliRepositoryTest {
	@Autowired
	private ArticoliRepository articoloRepository;

	@Test
	@Order(1)
	public void A_TestInsArticolo() {
		Articolo articolo = this.createArticolo();
		this.articoloRepository.save(articolo);

		assertThat(this.articoloRepository.findByCodArt("123Test"))
			.extracting(Articolo::getDescrizione)
			.isEqualTo("Articolo di Test");
	}

	private Articolo createArticolo() {
		Articolo articolo = new Articolo();
		articolo.setCodice("123Test");
		articolo.setDescrizione("Articolo di Test");
		articolo.setFamigliaAssortimento(this.createFamigliaAssortimento());
		articolo.setBarcodes(this.createBarcodesByArticolo(articolo));
		
		return articolo;
		
	}
	private FamigliaAssortimento createFamigliaAssortimento() {
		FamigliaAssortimento famigliaAssortimento = new FamigliaAssortimento();
		famigliaAssortimento.setCodice(1);

		return famigliaAssortimento;
	}
	
	private Set<Barcode> createBarcodesByArticolo(final Articolo articolo) {
		Set<Barcode> barCodes = new HashSet<>();
		Barcode barcode = new Barcode();
		barcode.setArticolo(articolo);
		barcode.setBarcode("CP");
		barcode.setIdTipoArticolo(121312);
		barCodes.add(barcode);
		
		return barCodes;

	}
	
	@Test
	@Order(2)
	public void B_TestSelByDescrizioneLike() {
		List<Articolo> articoli = this.articoloRepository.findByDescrizioneLike("Articolo di Test");
		assertEquals(1, articoli.size());
	}

	@Test
	@Order(3)
	public void C_TestfindByBarcodes() throws Exception {
		assertThat(this.articoloRepository.SelByEan("12345678"))
				.extracting(Articolo::getDescrizione)
				.isEqualTo("Articolo di Test");

	}

	@Test
	@Order(4)
	public void D_TestDelArticolo() {
		Articolo articolo = this.articoloRepository.findByCodArt("123Test");
		this.articoloRepository.delete(articolo);
	}

}