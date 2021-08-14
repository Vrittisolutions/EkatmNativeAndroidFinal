precision mediump float;
struct MaterialInfo {
  highp vec4 m_DiffuseColor;
  highp vec4 m_SpecularColor;
  highp vec2 m_SpecularPowerBumpiness;
};
uniform highp vec3 CameraPosition;
uniform highp vec4 AmbientLightColor;
uniform MaterialInfo Material;
uniform highp mat4 Light0;
varying highp vec4 xlv_COLOR0;
varying highp vec4 xlv_TEXCOORD1;
varying highp vec3 xlv_TEXCOORD2;
void main ()
{
  highp vec4 spec_1;
  highp vec4 baseColor_2;
  highp vec3 tmpvar_3;
  tmpvar_3 = normalize(xlv_TEXCOORD2);
  baseColor_2 = (Material.m_DiffuseColor * xlv_COLOR0);
  spec_1.xyz = Material.m_SpecularColor.xyz;
  spec_1.w = Material.m_SpecularPowerBumpiness.x;
  highp vec4 finalColor_4;
  highp vec4 tmpvar_5;
  tmpvar_5 = (baseColor_2 * AmbientLightColor);
  highp vec4 v_6;
  v_6.x = Light0[0].y;
  v_6.y = Light0[1].y;
  v_6.z = Light0[2].y;
  v_6.w = Light0[3].y;
  highp vec4 v_7;
  v_7.x = Light0[0].x;
  v_7.y = Light0[1].x;
  v_7.z = Light0[2].x;
  v_7.w = Light0[3].x;
  highp vec3 tmpvar_8;
  tmpvar_8 = normalize(-(v_7.xyz));
  finalColor_4.xyz = (tmpvar_5 + ((
    (baseColor_2 * max (dot (tmpvar_3, tmpvar_8), 0.0))
   + 
    (spec_1 * pow (clamp (dot (
      (tmpvar_8 - (2.0 * (dot (tmpvar_3, tmpvar_8) * tmpvar_3)))
    , 
      -(normalize(normalize((CameraPosition - xlv_TEXCOORD1.xyz))))
    ), 0.0, 1.0), spec_1.w))
  ) * v_6)).xyz;
  finalColor_4.w = tmpvar_5.w;
  gl_FragData[0] = finalColor_4;
}

