package com.yotereparo.service;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yotereparo.dao.ServiceTypeDaoImpl;
import com.yotereparo.model.ServiceType;

/**
 * Capa de servicio para Ciudades.
 * El objetivo de la misma es servir de interfaz entre el modelo y la capa de acceso a datos,
 * expone métodos para uso público en el contexto de la aplicación.
 * 
 * Implementa lógica de negocio donde correspondiera.
 * 
 * @author Rodrigo Yanis
 * 
 */
@Service("serviceTypeService")
@Transactional
public class ServiceTypeServiceImpl implements ServiceTypeService {
	
	private static final Logger logger = LogManager.getLogger(ServiceTypeServiceImpl.class);
	
	@Autowired
	private ServiceTypeDaoImpl dao;

	public List<ServiceType> getAllServiceTypes() {
		logger.debug("Fetching all service types");
		return dao.getAllServiceTypes();
	}

	public ServiceType getServiceTypeById(Integer id) {
		logger.debug(String.format("Fetching service type by id <%s>", id));
		return dao.getServiceTypeById(id);
	}
	
	public ServiceType getServiceTypeByDescription(String description) {
		logger.debug(String.format("Fetching service type <%s>", description));
		return dao.getServiceTypeByDescription(description);
	}
	
	public boolean exist(Integer id) {
		logger.debug(String.format("Verifying existence of service type with id <%s>", id));
		return (getServiceTypeById(id) != null);
	}
	
	public boolean exist(String description) {
		logger.debug(String.format("Verifying existence of service type <%s>", description));
		return (getServiceTypeByDescription(description) != null);
	}
}