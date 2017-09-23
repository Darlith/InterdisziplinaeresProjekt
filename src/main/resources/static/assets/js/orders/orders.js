$(function() {
    var endPoints = {
        getOrders: 'partials/orders',
        addSampleOrders: 'partials/addSampleOrders'
    };

    // load already processed orders
    getOrders();

    // Register click handlers
    $('#uploadCsvBtn').click(handleCsvSubmit);
    $('#uploadDemoBtn').click(handleDemoSubmit);
    $('#deleteOrderData').click(handleDeleteOrderDataSubmit);

    function handleCsvSubmit(e) {
        alert('Feature in arbeit!');
    }

    function handleDeleteOrderDataSubmit() {
        var uri = '/orders/delete';
        $.ajax({
            type: 'DELETE',
            url: uri,
            success: function() {
                showNotification("Die Bestellungen wurden gelöscht", 'danger');
                getOrders();
            }
        })
    }

    function handleDemoSubmit(e) {
        showNotification("Die Bestellungen werden verabeitet");
        $('#uploadDemoBtn').html('Verarbeite Daten...');
        $('#orderList').load(endPoints.addSampleOrders, function() {
            showNotification("Die Bestellungen wurden erfolgreich verarbeitet");
            $('#uploadDemoBtn').html('Abschicken');
        });
    }

    function getOrders() {
        $('#orderList').load(endPoints.getOrders);
    }

    function showNotification(msg, type) {
        type = type || 'success';
        $.notify({
            icon: "notifications",
            message: msg
        },{
            type: type,
            timer: 3000,
            placement: {
                from: 'top',
                align: 'right'
            }
        });

    }
});
