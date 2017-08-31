jQuery(function($) {
	// $.post("test.php", { name: "John", time: "2pm" } );
	// $.ajax("../meter", {
	// // type : "PUT",
	// dataType : "json",
	// // method : "PUT",
	// data : {
	// resourceid : "123,3,2",
	// terminalid : "termilid",
	// action : "connect",
	// _method : "put",
	// }
	// }, function(data) {
	// alert("Data Loaded: " + data);
	// });
	var url = 'parkplaces'

	$("#getbtn").click(function() {
		var entity = {};
		$.get(url + '?page=3&per_page=20', entity, function(result) {
			console.log("success");
		}, "json")
	});

	$("#postbtn").click(function() {
		var entity = {
			name : '123',
			parentId : "",
			regionLevel : 0
		// ,
		// _method : "post"
		};
		console.log(entity);
		$.ajax(url, {
			dataType : "json",
			method : "POST",
			contentType : "application/json",
			data : JSON.stringify(entity)
		})
	});

	$("#modifybtn").click(function() {
		var entity = {
			id : '8a89aa10523e92ed01523e92ed4f0000',
			name : 'modifyed',
		// _method:'put'
		};
		console.log(entity);
		$.ajax(url + '/123', {
			dataType : "json",
			type : "PUT",
			contentType : "application/json",
			data : JSON.stringify(entity)
		})
	});

	$("#delbtn").click(function() {
		// var entity = {
		// id : [ '8a89aa10523e92ed01523e92ed4f0000' ]
		// };
		console.log('delete');
		$.ajax(url + '/11', {
			dataType : "json",
			type : 'delete',
			contentType : "application/json"
		})
	});

	$("#getPayBtn").click(function() {
		$.ajax('/site/user/PKD201603100000000002', {
			dataType : "json",
			type : "PUT",
			contentType : "application/json"
		})
	});

	$("#payBtn").click(function() {
		var entity = {
			payNo : '8a89aa10523e92ed01523e92ed4f0000',
			Flow : 'modifyed',
			amount : 'modifyed',
			lockTime : '2016-01-16 14:41:57'
		// _method:'put'
		};
		console.log(entity);
		$.ajax('/site/pay/8a89aa10523e92ed01523e92ed4f0000', {
			dataType : "json",
			type : "PUT",
			contentType : "application/json",
			data : JSON.stringify(entity)
		})
	});

})
