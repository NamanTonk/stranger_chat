package com.newEra.strangers.chat.model;

import java.util.List;

public class profile_Model {

    /**
     * results : [{"gender":"female","name":{"title":"Ms","first":"Susanna","last":"Hayes"},"location":{"street":{"number":9347,"name":"North Road"},"city":"Portmarnock","state":"Roscommon","country":"Ireland","postcode":63369,"coordinates":{"latitude":"23.9265","longitude":"42.2777"},"timezone":{"offset":"-12:00","description":"Eniwetok, Kwajalein"}},"email":"susanna.hayes@example.com","login":{"uuid":"e6a0e7cc-907c-4efb-9f4d-297616fcbf2d","username":"tinycat644","password":"dallas1","salt":"bCV30QZ8","md5":"6c40b0b702b9f78acc62c1eb378342aa","sha1":"1e58de15905974ebfd055337e2736d28894f0ec0","sha256":"68985a2a99c15f7719e757b7c5076336a8a8a7a482722e6b515c7d71ffce5519"},"dob":{"date":"1968-09-18T17:30:09.140Z","age":52},"registered":{"date":"2009-04-20T11:14:47.766Z","age":11},"phone":"031-977-9189","cell":"081-876-7875","id":{"name":"PPS","value":"8780939T"},"picture":{"large":"https://randomuser.me/api/portraits/women/10.jpg","medium":"https://randomuser.me/api/portraits/med/women/10.jpg","thumbnail":"https://randomuser.me/api/portraits/thumb/women/10.jpg"},"nat":"IE"}]
     * info : {"seed":"3019de2b30c4056f","results":1,"page":1,"version":"1.3"}
     */

    private InfoBean info;
    private List<ResultsBean> results;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class InfoBean {
        /**
         * seed : 3019de2b30c4056f
         * results : 1
         * page : 1
         * version : 1.3
         */

        private String seed;
        private int results;
        private int page;
        private String version;

        public String getSeed() {
            return seed;
        }

        public void setSeed(String seed) {
            this.seed = seed;
        }

        public int getResults() {
            return results;
        }

        public void setResults(int results) {
            this.results = results;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }

    public static class ResultsBean {
        /**
         * gender : female
         * name : {"title":"Ms","first":"Susanna","last":"Hayes"}
         * location : {"street":{"number":9347,"name":"North Road"},"city":"Portmarnock","state":"Roscommon","country":"Ireland","postcode":63369,"coordinates":{"latitude":"23.9265","longitude":"42.2777"},"timezone":{"offset":"-12:00","description":"Eniwetok, Kwajalein"}}
         * email : susanna.hayes@example.com
         * login : {"uuid":"e6a0e7cc-907c-4efb-9f4d-297616fcbf2d","username":"tinycat644","password":"dallas1","salt":"bCV30QZ8","md5":"6c40b0b702b9f78acc62c1eb378342aa","sha1":"1e58de15905974ebfd055337e2736d28894f0ec0","sha256":"68985a2a99c15f7719e757b7c5076336a8a8a7a482722e6b515c7d71ffce5519"}
         * dob : {"date":"1968-09-18T17:30:09.140Z","age":52}
         * registered : {"date":"2009-04-20T11:14:47.766Z","age":11}
         * phone : 031-977-9189
         * cell : 081-876-7875
         * id : {"name":"PPS","value":"8780939T"}
         * picture : {"large":"https://randomuser.me/api/portraits/women/10.jpg","medium":"https://randomuser.me/api/portraits/med/women/10.jpg","thumbnail":"https://randomuser.me/api/portraits/thumb/women/10.jpg"}
         * nat : IE
         */

        private String gender;
        private NameBean name;
        private LocationBean location;
        private String email;
        private LoginBean login;
        private DobBean dob;
        private RegisteredBean registered;
        private String phone;
        private String cell;
        private IdBean id;
        private PictureBean picture;
        private String nat;

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public NameBean getName() {
            return name;
        }

        public void setName(NameBean name) {
            this.name = name;
        }

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public LoginBean getLogin() {
            return login;
        }

        public void setLogin(LoginBean login) {
            this.login = login;
        }

        public DobBean getDob() {
            return dob;
        }

        public void setDob(DobBean dob) {
            this.dob = dob;
        }

        public RegisteredBean getRegistered() {
            return registered;
        }

        public void setRegistered(RegisteredBean registered) {
            this.registered = registered;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCell() {
            return cell;
        }

        public void setCell(String cell) {
            this.cell = cell;
        }

        public IdBean getId() {
            return id;
        }

        public void setId(IdBean id) {
            this.id = id;
        }

        public PictureBean getPicture() {
            return picture;
        }

        public void setPicture(PictureBean picture) {
            this.picture = picture;
        }

        public String getNat() {
            return nat;
        }

        public void setNat(String nat) {
            this.nat = nat;
        }

        public static class NameBean {
            /**
             * title : Ms
             * first : Susanna
             * last : Hayes
             */

            private String title;
            private String first;
            private String last;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getFirst() {
                return first;
            }

            public void setFirst(String first) {
                this.first = first;
            }

            public String getLast() {
                return last;
            }

            public void setLast(String last) {
                this.last = last;
            }
        }

        public static class LocationBean {
            /**
             * street : {"number":9347,"name":"North Road"}
             * city : Portmarnock
             * state : Roscommon
             * country : Ireland
             * postcode : 63369
             * coordinates : {"latitude":"23.9265","longitude":"42.2777"}
             * timezone : {"offset":"-12:00","description":"Eniwetok, Kwajalein"}
             */

            private StreetBean street;
            private String city;
            private String state;
            private String country;
            private int postcode;
            private CoordinatesBean coordinates;
            private TimezoneBean timezone;

            public StreetBean getStreet() {
                return street;
            }

            public void setStreet(StreetBean street) {
                this.street = street;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public int getPostcode() {
                return postcode;
            }

            public void setPostcode(int postcode) {
                this.postcode = postcode;
            }

            public CoordinatesBean getCoordinates() {
                return coordinates;
            }

            public void setCoordinates(CoordinatesBean coordinates) {
                this.coordinates = coordinates;
            }

            public TimezoneBean getTimezone() {
                return timezone;
            }

            public void setTimezone(TimezoneBean timezone) {
                this.timezone = timezone;
            }

            public static class StreetBean {
                /**
                 * number : 9347
                 * name : North Road
                 */

                private int number;
                private String name;

                public int getNumber() {
                    return number;
                }

                public void setNumber(int number) {
                    this.number = number;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class CoordinatesBean {
                /**
                 * latitude : 23.9265
                 * longitude : 42.2777
                 */

                private String latitude;
                private String longitude;

                public String getLatitude() {
                    return latitude;
                }

                public void setLatitude(String latitude) {
                    this.latitude = latitude;
                }

                public String getLongitude() {
                    return longitude;
                }

                public void setLongitude(String longitude) {
                    this.longitude = longitude;
                }
            }

            public static class TimezoneBean {
                /**
                 * offset : -12:00
                 * description : Eniwetok, Kwajalein
                 */

                private String offset;
                private String description;

                public String getOffset() {
                    return offset;
                }

                public void setOffset(String offset) {
                    this.offset = offset;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }
            }
        }

        public static class LoginBean {
            /**
             * uuid : e6a0e7cc-907c-4efb-9f4d-297616fcbf2d
             * username : tinycat644
             * password : dallas1
             * salt : bCV30QZ8
             * md5 : 6c40b0b702b9f78acc62c1eb378342aa
             * sha1 : 1e58de15905974ebfd055337e2736d28894f0ec0
             * sha256 : 68985a2a99c15f7719e757b7c5076336a8a8a7a482722e6b515c7d71ffce5519
             */

            private String uuid;
            private String username;
            private String password;
            private String salt;
            private String md5;
            private String sha1;
            private String sha256;

            public String getUuid() {
                return uuid;
            }

            public void setUuid(String uuid) {
                this.uuid = uuid;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getSalt() {
                return salt;
            }

            public void setSalt(String salt) {
                this.salt = salt;
            }

            public String getMd5() {
                return md5;
            }

            public void setMd5(String md5) {
                this.md5 = md5;
            }

            public String getSha1() {
                return sha1;
            }

            public void setSha1(String sha1) {
                this.sha1 = sha1;
            }

            public String getSha256() {
                return sha256;
            }

            public void setSha256(String sha256) {
                this.sha256 = sha256;
            }
        }

        public static class DobBean {
            /**
             * date : 1968-09-18T17:30:09.140Z
             * age : 52
             */

            private String date;
            private int age;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public int getAge() {
                return age;
            }

            public void setAge(int age) {
                this.age = age;
            }
        }

        public static class RegisteredBean {
            /**
             * date : 2009-04-20T11:14:47.766Z
             * age : 11
             */

            private String date;
            private int age;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public int getAge() {
                return age;
            }

            public void setAge(int age) {
                this.age = age;
            }
        }

        public static class IdBean {
            /**
             * name : PPS
             * value : 8780939T
             */

            private String name;
            private String value;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public static class PictureBean {
            /**
             * large : https://randomuser.me/api/portraits/women/10.jpg
             * medium : https://randomuser.me/api/portraits/med/women/10.jpg
             * thumbnail : https://randomuser.me/api/portraits/thumb/women/10.jpg
             */

            private String large;
            private String medium;
            private String thumbnail;

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }

            public String getMedium() {
                return medium;
            }

            public void setMedium(String medium) {
                this.medium = medium;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }
        }
    }
}
