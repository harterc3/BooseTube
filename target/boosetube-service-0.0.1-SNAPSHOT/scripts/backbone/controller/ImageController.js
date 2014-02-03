var ImageController = {
	
	init : function() {
		var self = this;
		var imageId = getQueryStringParam("imageId");
		if (imageId == null) return;
		
		this.image = new App.Model.Content({_id: imageId});
		this.image.fetch({
			success : function(result) {
				new App.View.Image( { model : result });
				self.fillMeta();
			}
		});
	},
	
	fillMeta : function() {
		if (this.image == null) return;
		
		$("meta[property='og:title']").attr("content", "BooseTube | " + this.image.attributes.name);
		$("meta[property='og:description']").attr("content", this.image.attributes.description);
		$("meta[property='og:image']").attr("content", "http://www.boosetube.com/" + this.image.attributes.filepath);
		$("meta[property='og:url']").attr("content", document.URL);
	}
};