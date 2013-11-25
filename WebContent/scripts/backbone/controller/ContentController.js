var ContentController = {
	init : function() {
		var collection = new App.Collection.ContentCollection();
		collection.fetch({
			success : function(result) {
				var imageList = new App.View.ImageList( { model : result.models });
/*
				result.models.forEach(function(item) {
					var type = item.attributes.type;
					switch (type) {
					case "image":
						var image = new App.View.Image({
							model : item.attributes
						});
						imageList.addImage(image)
						break;
					default:
					}
				});*/
			}
		});
	}
};