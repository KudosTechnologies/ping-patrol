export const handleLogError = (error: any) => {
    if (error.response) {
        console.error("Error response", error.response.data);
    } else if (error.request) {
        console.error("Error request", error.request);
    } else {
        console.error("Error", error.message);
    }
};
