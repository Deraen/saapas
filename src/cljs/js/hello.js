export var sayHello = function() {
    console.log("Hello, world!");
};
export var sayThings = function(xs) {
    for(let x of xs) {
        console.log(x);
    }
};
export var reactHello = function() {
    return <p>Hello from JSX!</p>;
};
