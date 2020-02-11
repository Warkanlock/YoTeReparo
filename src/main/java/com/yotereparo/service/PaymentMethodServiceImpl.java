package com.yotereparo.service;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yotereparo.dao.PaymentMethodDaoImpl;
import com.yotereparo.model.PaymentMethod;

/**
 * Capa de servicio para Medios de Pago.
 * El objetivo de la misma es servir de interfaz entre el modelo y la capa de acceso a datos,
 * expone métodos para uso público en el contexto de la aplicación.
 * 
 * Implementa lógica de negocio donde correspondiera.
 * 
 * @author Rodrigo Yanis
 * 
 */
@Service("paymentMethodService")
@Transactional 
public class PaymentMethodServiceImpl implements PaymentMethodService {
	
	private static final Logger logger = LogManager.getLogger(PaymentMethodServiceImpl.class);
	
	@Autowired
	private PaymentMethodDaoImpl dao;

	public List<PaymentMethod> getAllPaymentMethods() {
		logger.debug("Fetching all payment methods");
		return dao.getAllPaymentMethods();
	}

	public PaymentMethod getPaymentMethodById(Integer id) {
		logger.debug(String.format("Fetching payment method by id <%s>", id));
		return dao.getPaymentMethodById(id);
	}
	
	public PaymentMethod getPaymentMethodByDescription(String description) {
		logger.debug(String.format("Fetching payment method <%s>", description));
		return dao.getPaymentMethodByDescription(description);
	}
	
	public boolean exist(Integer id) {
		logger.debug(String.format("Verifying existence of payment method with id <%s>", id));
		return (getPaymentMethodById(id) != null);
	}
	
	public boolean exist(String description) {
		logger.debug(String.format("Verifying existence of payment method <%s>", description));
		return (getPaymentMethodByDescription(description) != null);
	}
}