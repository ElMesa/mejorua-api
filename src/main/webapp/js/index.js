MejorUAFactory = function MejorUAFactory() {

	this.api = {};

	this.init = function init() {

		_.bindAll(this, "getIncidencia");
		_.bindAll(this, "onGetIncidenciaResponse");
		_.bindAll(this, "putIncidencia");
		_.bindAll(this, "onPutIncidenciaResponse");

		$("#fGet_submit").click(this.getIncidencia);
		$("#fPut_submit").click(this.putIncidencia);

		this.api.host = "api/"
		this.api.collection = $("#collection").val();
		this.api.collectionUrl = this.api.host + this.api.collection + "/";
	}

	this.getIncidencia = function getIncidencia() {

		var resourceId = $("#fGet_resourceId").val();

		var resourceUrl = this.api.collectionUrl + resourceId;

		$.ajax({
			url : resourceUrl,
			type : "GET",
			dataType: 'json',
		// success : this.onGetIncidenciaResponse,
		// error : this.onAjaxError
		})
		.done(this.onGetIncidenciaResponse)
		.fail(this.onAjaxError);
	}

	this.onGetIncidenciaResponse = function onGetIncidenciaResponse(data, status, xhr) {
		console.log("mejorua.onGetIncidenciaResponse(data %O, status %O, xhr %O)", data, status, xhr);

		$("#fGet_response").html(xhr.responseText);
		
		//$("#fPut_resourceId").val(data.id)
	}

	this.putIncidencia = function putIncidencia() {
		
		var incidencia = {};
		
		incidencia.id = $("#fPut_resourceId").val();
		incidencia.latitud = $("#fPut_latitude").val();
		incidencia.longitud = $("#fPut_longitude").val();
		incidencia.termino = $("#fPut_term").val();
		incidencia.accion = $("#fPut_action").val();

		var resourceUrl = this.api.collectionUrl + incidencia.id;
		var JSONdata = JSON.stringify(incidencia);

		$.ajax({
			url : resourceUrl,
			type : "PUT",
			//dataType: 'application/json',
			dataType: 'json',
			data : JSONdata
		})
		.done(this.onPutIncidenciaResponse)
		.fail(this.onAjaxError);

	}
	
	this.onPutIncidenciaResponse = function onPutIncidenciaResponse(data, status, xhr) {
		console.log("mejorua.onPutIncidenciaResponse(data %O, status %O, xhr %O)", data, status, xhr);

		$("#fPut_response").html(xhr.responseText);		
	}
	
	this.onAjaxError = function onAjaxError(xhr, status, error) {
		console.log("mejorua.onAjaxError(xhr %O, status %O, error %O)", xhr, status, error);
	}

}

$(document).ready(function() {

	mejorua = new MejorUAFactory();

	mejorua.init();
});