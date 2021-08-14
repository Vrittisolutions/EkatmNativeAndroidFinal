uniform highp vec4 screenSize;
uniform highp mat4 matWorldViewProj;
attribute highp vec4 vPosition;
attribute highp vec4 vNormal;
attribute highp vec3 vTexCoord0;
attribute highp vec4 vTexCoord1;
attribute highp vec4 vColor;
attribute highp vec4 vColor1;
varying highp vec4 xlv_COLOR0;
varying highp vec2 xlv_TEXCOORD0;
void main ()
{
  highp vec4 tmpvar_1;
  highp vec3 tmpvar_2;
  tmpvar_2.x = (vTexCoord0.x * vTexCoord1.z);
  tmpvar_2.y = vTexCoord1.x;
  tmpvar_2.z = vTexCoord0.z;
  tmpvar_1.xyz = mix (vPosition, vNormal, vTexCoord1.yyyy).xyz;
  tmpvar_1.w = 1.0;
  highp vec2 tmpvar_3;
  highp vec4 outPosition_4;
  highp vec4 tmpvar_5;
  tmpvar_5.w = 1.0;
  tmpvar_5.xyz = tmpvar_1.xyz;
  highp vec4 tmpvar_6;
  tmpvar_6 = (tmpvar_5 * matWorldViewProj);
  outPosition_4 = (tmpvar_6 / abs(tmpvar_6.w));
  highp vec4 tmpvar_7;
  tmpvar_7.w = 1.0;
  tmpvar_7.xyz = mix (vNormal, vPosition, vTexCoord1.yyyy).xyz;
  highp vec4 tmpvar_8;
  tmpvar_8 = (tmpvar_7 * matWorldViewProj);
  highp vec2 tmpvar_9;
  tmpvar_9 = normalize(((tmpvar_8 / 
    abs(tmpvar_8.w)
  ).xy - outPosition_4.xy));
  outPosition_4.x = (outPosition_4.x - ((tmpvar_2.x * screenSize.z) * tmpvar_9.y));
  outPosition_4.y = (outPosition_4.y + ((tmpvar_2.x * screenSize.w) * tmpvar_9.x));
  tmpvar_3.x = (0.5 * (1.0 - (
    (vTexCoord0.z - 2.0)
   / vTexCoord0.z)));
  tmpvar_3.y = tmpvar_2.y;
  gl_Position = outPosition_4;
  xlv_COLOR0 = mix (vColor, vColor1, vTexCoord1.yyyy);
  xlv_TEXCOORD0 = tmpvar_3;
}

