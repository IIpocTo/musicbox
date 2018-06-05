export default function timeout(delay, result) {
    return new Promise(resolve => {
        setTimeout(() => resolve(result), delay);
    });
}
