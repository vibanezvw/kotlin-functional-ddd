print("Waiting for mongo db...");
sleep(5000);

try {
    let status = rs.status();
    if (status.ok) {
        print("RS up and running...");
    }
} catch (e) {
    let rsConfig = {
        _id: "rs0",
        members: [
            {_id: 0, host: "localhost:27017"}
        ]
    };

    rs.initiate(rsConfig);

    let maxAttempts = 60;
    let attempts = 0;

    while (attempts < maxAttempts) {
        try {
            sleep(1000);
            let rsStatus = rs.status();

            if (rsStatus.members && rsStatus.members[0].stateStr === "PRIMARY") {
                print("Primary node is up.");
                break;
            }
        } catch (e) {
            print("Waiting for setup: " + e);
        }
        attempts++;
    }

    if (attempts >= maxAttempts) {
        print("ERROR: Impossible to set primary after:" + maxAttempts + " intents.");
        quit(1);
    }
}