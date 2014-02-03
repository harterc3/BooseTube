var ContentController = {
	collection : null,
	thumbnailList : null,
	modal : null,
	
	init : function() {
		new App.View.SearchControl({controller:this});
		this.modal = new App.View.Modal({el:$('#contentModal'),controller:this});
		this.collection = new App.Collection.ContentCollection();
		var self = this;
		this.collection.fetch({
			success : function(result) {
				this.thumbnailList = new App.View.ThumbnailList( { model : result.models, controller : self });
			}
		});
	},
	
	populateModal : function(model) {
		this.modal.updateModal(model);
	},
	
	search : function(searchText) {
		this.collection.reset();
		this.collection.fetch({
			data: $.param({name: searchText}),
			success : function(result) {
				this.thumbnailList = new App.View.ThumbnailList( { model : result.models, controller : self });
			}
		});
	}
};