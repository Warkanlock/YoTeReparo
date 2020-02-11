package com.yotereparo.service;

import java.util.List;
import java.util.Locale;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;

import com.yotereparo.dao.ServiceDaoImpl;
import com.yotereparo.model.City;
import com.yotereparo.model.District;
import com.yotereparo.model.PaymentMethod;
import com.yotereparo.model.Service;
import com.yotereparo.model.User;
import com.yotereparo.util.error.CustomResponseError;

/**
 * Capa de servicio para Servicios.
 * El objetivo de la misma es servir de interfaz entre el modelo y la capa de acceso a datos,
 * expone métodos para uso público en el contexto de la aplicación.
 * 
 * Implementa lógica de negocio donde correspondiera.
 * 
 * @author Rodrigo Yanis
 * 
 */
@org.springframework.stereotype.Service("serviceManager")
@Transactional 
public class ServiceManagerImpl implements ServiceManager {
	
	private static final Logger logger = LogManager.getLogger(ServiceManagerImpl.class);
	
	@Autowired
	private ServiceDaoImpl dao;
	@Autowired
    private MessageSource messageSource;
	@Autowired
    private UserService userService;
	
	@Override
	public void createService(Service service) {
		if (userService.isPrestador(service.getUsuarioPrestador())) {
			service.setFechaCreacion(new DateTime());
			// No cargamos imagenes en tiempo de creacion, siempre usar el metodo dedicado
			service.setImagen(null);
			service.setThumbnail(null);
			service.setEstado("ACTIVO");
			
			logger.info(String.format("Commiting creation of service <%s>", service.getId()));
			dao.persist(service);
		}
		else {
			logger.info(String.format("Service <%s> can't be created. User <%s> is not of type Prestador", service.getId(), service.getUsuarioPrestador().getId()));
			throw new CustomResponseError("Service","usuarioPrestador",messageSource.getMessage("service.usuarioPrestador.unauthorized", new String[]{service.getUsuarioPrestador().getId()}, Locale.getDefault()));
		}
			
	}
	
	@Override
	public void updateService(Service service) {
		Service entity = getServiceById(service.getId());
		
		if (!service.getUsuarioPrestador().getId().equals(entity.getUsuarioPrestador().getId())) {
			// Illegal
			logger.debug(String.format("Service <%s> owner: <%s> can't be modified!", service.getId(), entity.getUsuarioPrestador().getId()));
			throw new CustomResponseError("Service","usuarioPrestador",messageSource.getMessage("service.usuarioPrestador.cant.change", null, Locale.getDefault()));
		}
		
		if (!service.getDescripcion().equals(entity.getDescripcion())) {
			logger.debug(String.format("Updating attribute 'Descripcion' from service <%s>", service.getId()));
			entity.setDescripcion(service.getDescripcion());
		}
		
		if (service.getDisponibilidad() != null) {
			if (!service.getDisponibilidad().equals(entity.getDisponibilidad())) {
				logger.debug(String.format("Updating attribute 'Disponibilidad' from service <%s>", service.getId()));
				entity.setDisponibilidad(service.getDisponibilidad());
			}
		}
		else {
			if (entity.getDisponibilidad() != null) {
				logger.debug(String.format("Updating attribute 'Disponibilidad' from service <%s>", service.getId()));
				entity.setDisponibilidad(null);
			}
		}
		
		Boolean precioHasChanged = false;
		if (!service.getPrecioMaximo().equals(entity.getPrecioMaximo())) {
			logger.debug(String.format("Updating attribute 'PrecioMaximo' from service <%s>", service.getId()));
			entity.setPrecioMaximo(service.getPrecioMaximo());
			precioHasChanged = true;
		}
		
		if (!service.getPrecioMinimo().equals(entity.getPrecioMinimo())) {
			logger.debug(String.format("Updating attribute 'PrecioMinimo' from service <%s>", service.getId()));
			entity.setPrecioMinimo(service.getPrecioMinimo());
			precioHasChanged = true;
		}
		
		if (precioHasChanged) {
			logger.debug(String.format("Updating attribute 'PrecioPromedio' from service <%s>", service.getId()));
			entity.setPrecioPromedio(null);
		}
		
		if (service.getPrecioInsumos() != null) {
			if (!service.getPrecioInsumos().equals(entity.getPrecioInsumos())) {
				logger.debug(String.format("Updating attribute 'PrecioInsumos' from service <%s>", service.getId()));
				entity.setPrecioInsumos(service.getPrecioInsumos());
			}
		}
		else {
			if (entity.getPrecioInsumos() != null) {
				logger.debug(String.format("Updating attribute 'PrecioInsumos' from service <%s>", service.getId()));
				entity.setPrecioInsumos(null);
			}
		}
		
		if (service.getPrecioAdicionales() != null) {
			if (!service.getPrecioAdicionales().equals(entity.getPrecioAdicionales())) {
				logger.debug(String.format("Updating attribute 'PrecioAdicionales' from service <%s>", service.getId()));
				entity.setPrecioAdicionales(service.getPrecioAdicionales());
			}
		}
		else {
			if (entity.getPrecioAdicionales() != null) {
				logger.debug(String.format("Updating attribute 'PrecioAdicionales' from service <%s>", service.getId()));
				entity.setPrecioAdicionales(null);
			}
		}
		
		if (!service.getHorasEstimadasEjecucion().equals(entity.getHorasEstimadasEjecucion())) {
			logger.debug(String.format("Updating attribute 'HorasEstimadasEjecucion' from service <%s>", service.getId()));
			entity.setHorasEstimadasEjecucion(service.getHorasEstimadasEjecucion());
		}
		
		if (!service.getCantidadTrabajadores().equals(entity.getCantidadTrabajadores())) {
			logger.debug(String.format("Updating attribute 'CantidadTrabajadores' from service <%s>", service.getId()));
			entity.setCantidadTrabajadores(service.getCantidadTrabajadores());
		}
		
		if (!service.isFacturaEmitida() == entity.isFacturaEmitida()) {
			logger.debug(String.format("Updating attribute 'FacturaEmitida' from service <%s>", service.getId()));
			entity.setFacturaEmitida(service.isFacturaEmitida());
		}
		
		if (!service.getTipoServicio().equals(entity.getTipoServicio())) {
			logger.debug(String.format("Updating attribute 'TipoServicio' from service <%s>", service.getId()));
			entity.setTipoServicio(service.getTipoServicio());
		}
		
		if (!service.getMediosDePago().equals(entity.getMediosDePago())) {
			logger.debug(String.format("Updating attribute 'MediosDePago' from service <%s>", service.getId()));
			entity.getMediosDePago().clear();
			entity.setMediosDePago(service.getMediosDePago());
		}
		
		logger.info(String.format("Commiting update for service <%s>", service.getId()));
	}
	
	@Override
	public void enableServiceById(Integer id) {
		Service service = getServiceById(id);
		if (service != null && service.getEstado() != "ACTIVO") {
			logger.info(String.format("Enabling service <%s>", service.getId()));
			service.setEstado("ACTIVO");
		}
	}
	
	@Override
	public void disableServiceById(Integer id) {
		Service service = getServiceById(id);
		if (service != null && service.getEstado() != "INACTIVO") {
			logger.info(String.format("Disabling service <%s>", service.getId()));
			service.setEstado("INACTIVO");
		}
	}
	
	@Override
	public void deleteServiceById(Integer id) {
		logger.info(String.format("Commiting deletion of service <%s>", id));
		dao.deleteServiceById(id);
	}
	
	@Override
	public Service getServiceById(Integer id) {
		logger.debug(String.format("Fetching service <%s>", id));
		return dao.getByKey(id);
	}
	
	@Override
	public void updateServicePhotoById(Integer id, byte[] b64photo) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean exist(Service service) {
		logger.debug(String.format("Verifying existence of service with description <%s>", service.getDescripcion()));
		User relatedUser = service.getUsuarioPrestador();
		for (Service s : relatedUser.getServicios())
			if (service.equals(s)) 
				return true;
		return false;
	}
	
	@Override
	public boolean exist(Integer id) {
		logger.debug(String.format("Verifying existence of service with id <%s>", id));
		return (getServiceById(id) != null);
	}
	
	@Override
	public List<Service> getAllServices() {
		logger.debug("Fetching all services");
		return dao.getAllServices(null);
	}
	
	@Override
	public List<Service> getAllServices(User user) {
		logger.debug(String.format("Fetching all services of user <%s>", user.getId()));
		return dao.getAllServices(user);
	}
	
	@Override
	public List<Service> getAllServices(District district) {
		logger.debug(String.format("Fetching all services within district <%s>", district.getDescripcion()));
		return dao.getAllServices(district);
	}
	
	@Override
	public List<Service> getAllServices(City city) {
		logger.debug(String.format("Fetching all services within city <%s>", city.getDescripcion()));
		return dao.getAllServices(city);
	}
	
	@Override
	public List<Service> getAllServices(PaymentMethod paymentMethod) {
		logger.debug(String.format("Fetching all services accepting payment method <%s>", paymentMethod.getDescripcion()));
		return dao.getAllServices(paymentMethod);
	}
}
