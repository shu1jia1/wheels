#Builders

>可以用来改写我们的IOPDeviceMessage

当你的对象需要3个以上的构造参数时，使用builder来构造这个对象。写起来可能有点啰嗦但是扩展性和可读性都更好。如果你是创建一个值类型，考虑AutoValue。

    class Movie {
        static Builder newBuilder() {
            return new Builder();
        }
        static class Builder {
            String title;
            Builder withTitle(String title) {
                this.title = title;
                return this;
            }
            Movie build() {
                return new Movie(title);
            }
        }
     
        private Movie(String title) {
        [...]    
        }
    }
    // Use like this:
    Movie matrix = Movie.newBuilder().withTitle("The Matrix").build();