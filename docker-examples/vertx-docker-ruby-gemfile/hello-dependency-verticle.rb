require 'working_hours'
# https://github.com/Intrepidd/working_hours
# https://rubygems.org/gems/working_hours

# Configure working hours
WorkingHours::Config.working_hours = {
  :tue => {'09:00' => '12:00', '13:00' => '17:00'},
  :wed => {'09:00' => '12:00', '13:00' => '17:00'},
  :thu => {'09:00' => '12:00', '13:00' => '17:00'},
  :fri => {'09:00' => '12:00', '13:00' => '17:05:30'},
  :sat => {'19:00' => '24:00'}
}

# Configure timezone (uses activesupport, defaults to UTC)
WorkingHours::Config.time_zone = 'Eastern Time (US & Canada)'

# Configure holidays
WorkingHours::Config.holidays = [Date.new(2018, 02, 22)]


puts 1.working.day.from_now
puts 2.working.hours.from_now
puts 15.working.minutes.from_now