var AdminContentController = {
	collection : null,
	contentList : null,
	//modal : null,
	
	init : function() {
		//this.modal = new App.View.Modal({el:$('#contentModal'),controller:this});
		this.collection = new App.Collection.ContentCollection();
		var self = this;
		this.collection.fetch({
			success : function(result) {
				self.contentList = new App.View.AdminContentList( { model : result.models, controller : self });
			}
		});
	}
};