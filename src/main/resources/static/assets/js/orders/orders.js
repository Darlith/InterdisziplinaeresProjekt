$(function() {
    var endPoints = {
        getOrders: 'partials/orders',
        addSampleOrders: 'partials/addSampleOrders'
    };

    // load already processed orders
    getOrders();

    // Register click handlers
    $('#uploadCsvBtn').click(handleCsvSubmit);
    $('#uploadManualBtn').click(handleManualSubmit);
    $('#uploadDemoBtn').click(handleDemoSubmit);

    function handleCsvSubmit(e) {
        alert('ToDo');
    }

    function handleManualSubmit(e) {
        alert('ToDo');
    }

    function handleDemoSubmit(e) {
        $('#uploadDemoBtn').html('Verarbeite Daten...');
        $('#orderList').load(endPoints.addSampleOrders, function() {
            $('#uploadDemoBtn').html('Abschicken');
        });
    }

    function getOrders() {
        $('#orderList').load(endPoints.getOrders);
    }
});
